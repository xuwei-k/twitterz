package twitterz

import twitter4j.{TwitterException, Twitter}
import twitterz.Command._
import scala.collection.convert.decorateAsScala._
import scalaz.{EitherT, -\/, \/-, \/}

object Twitter4jInterpreterEither extends InterpreterF[EitherTInterpreter] {
  override def apply[A](command: Command[A]) =
    EitherT[({type λ[α] = Twitter => α})#λ, TwitterException, A]{
      (twitter: Twitter) =>

      try {
        \/-(Twitter4jInterpreter(command)(twitter))
      } catch {
        case e: TwitterException =>
          -\/(e)
      }
    }
}

object Twitter4jInterpreter extends (InterpreterF[({type l[a] = Twitter => a})#l]){
  override def apply[A](command: Command[A]) = twitter => command match {
    case Search(query) =>
      twitter.search(query)
    case GetSavedSearches =>
      twitter.getSavedSearches
    case UpdateStatus(tweet) =>
      twitter.updateStatus(tweet)
    case RetweetStatus(id) =>
      twitter.retweetStatus(id)
    case a: UpdateFriendshipById =>
      twitter.updateFriendship(a.userId, a.enableDeviceNotification, a.retweets)
    case a: UpdateFriendshipByScreenName =>
      twitter.updateFriendship(a.name, a.enableDeviceNotification, a.retweets)
    case GetRetweetsOfMe =>
      twitter.getRetweetsOfMe
    case GetRetweets(id) =>
      twitter.getRetweets(id)
    case GetHomeTimeline =>
      twitter.getHomeTimeline
    case GetBlockIds =>
      twitter.getBlocksIDs
    case GetMentionsTimeline =>
      twitter.getMentionsTimeline
    case GetUserTimeline =>
      twitter.getUserTimeline
    case GetDirectMessages =>
      twitter.getDirectMessages
    case GetSentDirectMessages =>
      twitter.getSentDirectMessages
    case UpdateProfileImage(image) =>
      twitter.updateProfileImage(image)
    case UpdateProfileImageByStream(stream) =>
      twitter.updateProfileImage(stream)
    case UpdateProfileBanner(image) =>
      twitter.updateProfileBanner(image)
    case UpdateProfileBannerByStream(stream) =>
      twitter.updateProfileBanner(stream)
    case UpdateProfileBackground(image, tile) =>
      twitter.updateProfileBackgroundImage(image, tile)
    case UpdateProfileBackgroundByStream(stream, tile) =>
      twitter.updateProfileBackgroundImage(stream, tile)
    case a: UpdateProfileColors =>
      twitter.updateProfileColors(a.profileBackgroundColor, a.profileTextColor, a.profileLinkColor, a.profileSidebarFillColor, a.profileSidebarBorderColor)
    case a: UpdateProfile =>
      twitter.updateProfile(a.name, a.url, a.location, a.description)
    case a: UpdateAccountSettings =>
      twitter.updateAccountSettings(a.trendLocationWoeid, a.sleepTimeEnabled, a.startSleepTime, a.endSleepTime, a.timeZone, a.lang)
    case CreateFavorite(id) =>
      twitter.createFavorite(id)
    case GetFavorites =>
      twitter.getFavorites
    case GetFavoritesById(id) =>
      twitter.getFavorites(id)
    case GetFavoritesByScreenName(name) =>
      twitter.getFavorites(name)
    case a: CreateUserListSubscriptionByListId =>
      twitter.createUserListSubscription(a.listId)
    case a: CreateUserListSubscriptionByOwnerId =>
      twitter.createUserListSubscription(a.ownerId, a.slug)
    case a: CreateUserListSubscriptionByScreenName =>
      twitter.createUserListSubscription(a.name, a.slug)
    case GetUserListsById(id) =>
      twitter.getUserLists(id)
    case GetUserListsByScreenName(name) =>
      twitter.getUserLists(name)
    case GetAvailableTrends =>
      twitter.getAvailableTrends
    case GetPlaceTriends(id) =>
      twitter.getPlaceTrends(id)
    case CreateBlockById(id) =>
      twitter.createBlock(id)
    case CreateBlockByScreenName(name) =>
      twitter.createBlock(name)
    case DestroyBlockById(id) =>
      twitter.destroyBlock(id)
    case DestroyBlockByScreenName(name) =>
      twitter.destroyBlock(name)
    case ReportSpanById(id) =>
      twitter.reportSpam(id)
    case ReportSpanByScreenName(name) =>
      twitter.reportSpam(name)
    case GetRateLimitStatus =>
      twitter.getRateLimitStatus.asScala.toMap
    case AnyCommand(function) =>
      function(twitter)
  }
}
