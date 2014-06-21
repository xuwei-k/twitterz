package twitterz

import twitter4j._
import java.io.{InputStream, File}

sealed abstract class Command[A]

object Command {

  final case class AnyCommand[A](function: Twitter => A) extends Command[A]

  final case class Search(query: Query) extends Command[QueryResult]
  case object GetSavedSearches extends Command[ResponseList[SavedSearch]]

  final case class UpdateFriendshipById(
    userId: Long, enableDeviceNotification: Boolean, retweets: Boolean
  ) extends Command[Relationship]
  final case class UpdateFriendshipByScreenName(
    name: String, enableDeviceNotification: Boolean, retweets: Boolean
  ) extends Command[Relationship]

  final case class ReportSpanById(id: Long) extends Command[User]
  final case class ReportSpanByScreenName(name: String) extends Command[User]

  final case class CreateBlockById(id: Long) extends Command[User]
  final case class CreateBlockByScreenName(name: String) extends Command[User]
  final case class DestroyBlockById(id: Long) extends Command[User]
  final case class DestroyBlockByScreenName(name: String) extends Command[User]
  case object GetBlockIds extends Command[IDs]

  final case class UpdateStatus(tweet: String) extends Command[Status]
  final case class RetweetStatus(id: Long) extends Command[Status]
  final case class GetRetweets(id: Long) extends Command[ResponseList[Status]]
  case object GetRetweetsOfMe extends Command[ResponseList[Status]]

  case object GetHomeTimeline extends Command[ResponseList[Status]]
  case object GetMentionsTimeline extends Command[ResponseList[Status]]
  case object GetUserTimeline extends Command[ResponseList[Status]]

  case object GetDirectMessages extends Command[ResponseList[DirectMessage]]
  case object GetSentDirectMessages extends Command[ResponseList[DirectMessage]]

  case object GetFavorites extends Command[ResponseList[Status]]
  final case class GetFavoritesById(id: Long) extends Command[ResponseList[Status]]
  final case class GetFavoritesByScreenName(name: String) extends Command[ResponseList[Status]]
  final case class CreateFavorite(id: Long) extends Command[Status]

  final case class GetUserListsById(id: Long) extends Command[ResponseList[UserList]]
  final case class GetUserListsByScreenName(name: String) extends Command[ResponseList[UserList]]

  final case class CreateUserListSubscriptionByListId(listId: Long) extends Command[UserList]
  final case class CreateUserListSubscriptionByOwnerId(ownerId: Long, slug: String) extends Command[UserList]
  final case class CreateUserListSubscriptionByScreenName(name: String, slug: String) extends Command[UserList]

  case object GetAvailableTrends extends Command[ResponseList[Location]]
  final case class GetPlaceTriends(id: Int) extends Command[Trends]

  final case class UpdateProfile(
    name: String, url: String, location: String, description: String
  ) extends Command[User]

  final case class UpdateProfileImage(file: File) extends Command[User]
  final case class UpdateProfileImageByStream(stream: InputStream) extends Command[User]
  final case class UpdateProfileBanner(file: File) extends Command[Unit]
  final case class UpdateProfileBannerByStream(stream: InputStream) extends Command[Unit]
  final case class UpdateProfileBackground(file: File, tile: Boolean) extends Command[User]
  final case class UpdateProfileBackgroundByStream(stream: InputStream, tile: Boolean) extends Command[User]
  final case class UpdateProfileColors(
    profileBackgroundColor: String,
    profileTextColor: String,
    profileLinkColor: String,
    profileSidebarFillColor: String,
    profileSidebarBorderColor: String
  ) extends Command[User]

  final case class UpdateAccountSettings(
    trendLocationWoeid: Int,
    sleepTimeEnabled: Boolean,
    startSleepTime: String,
    endSleepTime: String,
    timeZone: String,
    lang: String
  ) extends Command[AccountSettings]

  case object GetRateLimitStatus extends Command[Map[String, RateLimitStatus]]
}

