package ren.kujoka.profileChanger

import twitter4j._
import twitter4j.api._
import java.io.File
import com.typesafe.config._

object ProfileChanger {
  def main(args: Array[String]) = {
    val config = ConfigFactory.load
    val twitter: Twitter = new TwitterFactory().getInstance()
    var user: User = twitter.verifyCredentials()
    val listener: UserStreamListener = new UserStreamListener {
      def onStatus(status: Status) = {
        if (status.getUser() == user) {
          val tweet = status.getText().split(" ")
          if (tweet.length == 3 && tweet(0) == "@your id" && tweet(2).length == 1) {
            val num = tweet(2).toInt
            user = twitter.verifyCredentials()
            val footer = user.getDescription().dropWhile(_ != '★')
            try {
              val changeLists = config.getConfig(tweet(1))
              twitter.destroyStatus(status.getId)
              twitter.updateProfile(
                changeLists.getString("name"),
                changeLists.getString("url"),
                changeLists.getString("geo"),
                changeLists.getString("bio") + footer
              )
              twitter.updateProfileImage(
                new File("icon/" + changeLists.getList("icon").get(num).unwrapped)
              )
            } catch {
              case e: Exception => e.printStackTrace()
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
