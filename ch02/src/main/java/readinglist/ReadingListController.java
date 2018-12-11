package readinglist;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
@RequestMapping("/readingList")
public class ReadingListController {

  private static final String reader = "craig";
	private Logger logger = LoggerFactory.getLogger(getClass());

	private ReadingListRepository readingListRepository;

	@Autowired
	public ReadingListController(ReadingListRepository readingListRepository) {
		this.readingListRepository = readingListRepository;
	}

	@Autowired
	WebApplicationContext applicationContext;
	@GetMapping("/getAllUrl")
	@ResponseBody
	public List<String> getAllUrl(){
		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		//获取url与类和方法的对应信息
		Map<RequestMappingInfo,HandlerMethod> map = mapping.getHandlerMethods();
		List<String> urlList = new ArrayList<>();
		for (RequestMappingInfo info : map.keySet()){
			//获取url的Set集合，一个方法可能对应多个url
			Set<String> patterns = info.getPatternsCondition().getPatterns();
			for (String url : patterns){
				urlList.add(url);
			}
		}
		logger.info(Arrays.deepToString(urlList.toArray()));
		return urlList;
	}

	@RequestMapping(method=RequestMethod.GET)
	public String readersBooks(Model model) {
		logger.info("reading list ....");
		List<Book> readingList = readingListRepository.findByReader(reader);
		logger.info("reading list 2....");
		if (readingList != null) {
			model.addAttribute("books", readingList);
		}
		return "readingList";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String addToReadingList(Book book) {
		logger.info("reading add ....{}",book);
		book.setReader(reader);
		readingListRepository.save(book);

		return "redirect:/readingList";
	}
	
}
