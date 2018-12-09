@RestController
class HelloController {

  @RequestMapping("/")
  def hello() {
    "Hello World"
  }

}
//运行使用 spring run HelloController.groovy
//打开浏览器 localhost:8080 页面输出 'Hello World'