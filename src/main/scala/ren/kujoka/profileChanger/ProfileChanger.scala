package ren.kujoka.profileChanger

import twitter4j._
import twitter4j.api._
import scala.tools.reflect.ToolBox
import scala.reflect.runtime.currentMirror
import java.io.File

trait MyConfig {
  val account:String
  case class ChangeList(
    name:String,
    url:String,
    bio:String,
    geo:String,
    icon:Seq[File]
  )
  val changeLists:Map[String, ChangeList]
}

object ProfileChanger {
  def main(args: Array[String]) = {
    val toolbox = currentMirror.mkToolBox()
    val config: MyConfig = toolbox.eval(toolbox.parse(scala.io.Source.fromFile("config/MyConfig.scala").mkString)).asInstanceOf[MyConfig]
    val twitter: Twitter = new TwitterFactory().getInstance()
    var user: User = twitter.verifyCredentials()
    val listener: UserStreamListener = new UserStreamListener {
      def onStatus(status: Status) = {
        if (status.getUser() == user) {
          val tweet = status.getText().split(" ")
          if (tweet.length == 3 && tweet(0) == config.account && tweet(2).length == 1) {
            val num = tweet(2).toInt
            user = twitter.verifyCredentials()
            val footer = user.getDescription().dropWhile(_ != '★')
            try {
              if (config.changeLists.contains(tweet(1))) {
                twitter.destroyStatus(status.getId)
                twitter.updateProfile(
                  config.changeLists(tweet(1)).name,
                  config.changeLists(tweet(1)).url,
                  config.changeLists(tweet(1)).geo,
                  config.changeLists(tweet(1)).bio + footer
                )
                twitter.updateProfileImage(
                  config.changeLists(tweet(1)).icon(num)
                )
              }
            } catch {
              case e: Exception => println("更新に失敗しました")
            }
          }            
        }
      }
      def onDeletionNotice(s: StatusDeletionNotice) = {}
      def onTrackLimitationNotice(numberOfLimitedStatuses: Int) = {}
      def onException(ex: Exception) = ex.printStackTrace()
      def onScrubGeo(userId: Long, upToStatusId: Long) = {}
      def onStallWarning(arg0: StallWarning) = {}
      def onBlock(s: User, b: User) = {}
      def onDeletionNotice(x1: Long,x2: Long): Unit = {}
      def onDirectMessage(x1: DirectMessage): Unit = {}
      def onFavorite(x1: User,x2: User,x3: Status): Unit = {}
      def onFavoritedRetweet(x1: User,x2: User,x3: Status): Unit = {}
      def onFollow(x1: User,x2: User): Unit = {}
      def onFriendList(x1: Array[Long]): Unit = {}
      def onQuotedTweet(x1: User,x2: User,x3: Status): Unit = {}
      def onRetweetedRetweet(x1: User,x2: User,x3: Status): Unit = {}
      def onUnblock(x1: User,x2: User): Unit = {}
      def onUnfavorite(x1: User,x2: User,x3: Status): Unit = {}
      def onUnfollow(x1: User,x2: User): Unit = {}
      def onUserDeletion(x1: Long): Unit = {}
      def onUserListCreation(x1: User,x2: UserList): Unit = {}
      def onUserListDeletion(x1: User,x2: UserList): Unit = {}
      def onUserListMemberAddition(x1: User,x2: User,x3: UserList): Unit = {}
      def onUserListMemberDeletion(x1: User,x2: User,x3: UserList): Unit = {}
      def onUserListSubscription(x1: User,x2: User,x3: UserList): Unit = {}
      def onUserListUnsubscription(x1: User,x2: User,x3: UserList): Unit = {}
      def onUserListUpdate(x1: User,x2: UserList): Unit = {}
      def onUserProfileUpdate(x1: User): Unit = {}
      def onUserSuspension(x1: Long): Unit = {}
    }
    val twitterStream: TwitterStream = new TwitterStreamFactory().getInstance() 
    twitterStream.addListener(listener)
    twitterStream.user()
  }
}
