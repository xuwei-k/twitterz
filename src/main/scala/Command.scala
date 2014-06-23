package twitterz

import twitter4j._
import java.io.{InputStream, File}
import scalaz.liftFC

sealed abstract class Command[A]

object Command {

  @liftFC final case class AnyCommand[A](function: Twitter => A) extends Command[A]

  @liftFC final case class Search(query: Query) extends Command[QueryResult]

  @liftFC case object GetSavedSearches extends Command[ResponseList[SavedSearch]]

  @liftFC final case class UpdateFriendshipById(
    userId: Long, enableDeviceNotification: Boolean, retweets: Boolean
  ) extends Command[Relationship]
  @liftFC final case class UpdateFriendshipByScreenName(
    name: String, enableDeviceNotification: Boolean, retweets: Boolean
  ) extends Command[Relationship]

  @liftFC final case class ReportSpanById(id: Long) extends Command[User]
  @liftFC final case class ReportSpanByScreenName(name: String) extends Command[User]

  @liftFC final case class CreateBlockById(id: Long) extends Command[User]
  @liftFC final case class CreateBlockByScreenName(name: String) extends Command[User]
  @liftFC final case class DestroyBlockById(id: Long) extends Command[User]
  @liftFC final case class DestroyBlockByScreenName(name: String) extends Command[User]
  @liftFC case object GetBlockIds extends Command[IDs]

  @liftFC final case class UpdateStatus(tweet: String) extends Command[Status]
  @liftFC final case class RetweetStatus(id: Long) extends Command[Status]
  @liftFC final case class GetRetweets(id: Long) extends Command[ResponseList[Status]]
  @liftFC case object GetRetweetsOfMe extends Command[ResponseList[Status]]

  @liftFC case object GetHomeTimeline extends Command[ResponseList[Status]]
  @liftFC case object GetMentionsTimeline extends Command[ResponseList[Status]]
  @liftFC case object GetUserTimeline extends Command[ResponseList[Status]]

  @liftFC case object GetDirectMessages extends Command[ResponseList[DirectMessage]]
  @liftFC case object GetSentDirectMessages extends Command[ResponseList[DirectMessage]]

  @liftFC case object GetFavorites extends Command[ResponseList[Status]]
  @liftFC final case class GetFavoritesById(id: Long) extends Command[ResponseList[Status]]
  @liftFC final case class GetFavoritesByScreenName(name: String) extends Command[ResponseList[Status]]
  @liftFC final case class CreateFavorite(id: Long) extends Command[Status]

  @liftFC final case class GetUserListsById(id: Long) extends Command[ResponseList[UserList]]
  @liftFC final case class GetUserListsByScreenName(name: String) extends Command[ResponseList[UserList]]

  @liftFC final case class CreateUserListSubscriptionByListId(listId: Long) extends Command[UserList]
  @liftFC final case class CreateUserListSubscriptionByOwnerId(ownerId: Long, slug: String) extends Command[UserList]
  @liftFC final case class CreateUserListSubscriptionByScreenName(name: String, slug: String) extends Command[UserList]

  @liftFC case object GetAvailableTrends extends Command[ResponseList[Location]]
  @liftFC final case class GetPlaceTriends(id: Int) extends Command[Trends]

  @liftFC final case class UpdateProfile(
    name: String, url: String, location: String, description: String
  ) extends Command[User]

  @liftFC final case class UpdateProfileImage(file: File) extends Command[User]
  @liftFC final case class UpdateProfileImageByStream(stream: InputStream) extends Command[User]
  @liftFC final case class UpdateProfileBanner(file: File) extends Command[Unit]
  @liftFC final case class UpdateProfileBannerByStream(stream: InputStream) extends Command[Unit]
  @liftFC final case class UpdateProfileBackground(file: File, tile: Boolean) extends Command[User]
  @liftFC final case class UpdateProfileBackgroundByStream(stream: InputStream, tile: Boolean) extends Command[User]
  @liftFC final case class UpdateProfileColors(
    profileBackgroundColor: String,
    profileTextColor: String,
    profileLinkColor: String,
    profileSidebarFillColor: String,
    profileSidebarBorderColor: String
  ) extends Command[User]

  @liftFC final case class UpdateAccountSettings(
    trendLocationWoeid: Int,
    sleepTimeEnabled: Boolean,
    startSleepTime: String,
    endSleepTime: String,
    timeZone: String,
    lang: String
  ) extends Command[AccountSettings]

  @liftFC case object GetRateLimitStatus extends Command[Map[String, RateLimitStatus]]
}

