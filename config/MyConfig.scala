import ren.kujoka.profileChanger.MyConfig
import java.io.File

new MyConfig {
  val account = "@Your Account ID"
  val changeLists = Map(
    "Any String" -> ChangeList(
      name = "",
      url = "",
      geo = "",
      bio = "",
      icon = Seq(
        new File("hoge.png")
      )
    )
  )
}
