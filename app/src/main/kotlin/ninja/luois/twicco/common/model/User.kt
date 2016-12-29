package ninja.luois.twicco.common.model

import io.realm.RealmList
import io.realm.RealmObject

//open class UserEntities(
//        open var url: UrlEntity,
//        open var description: UrlEntity
//)

open class User(
        open var contributorsEnabled: Boolean = false,
        open var createdAt: String? = null,
        open var defaultProfile: Boolean? = null,
        open var defaultProfileImage: Boolean? = null,
        open var description: String? = null,
        open var email: String? = null,
        //open var entities: UserEntities,
        open var favouritesCount: Int? = null,
        open var followRequestSent: Boolean? = null,
        open var followersCount: Int? = null,
        open var friendsCount: Int? = null,
        open var geoEnabled: Boolean? = null,
        open var id: Long? = null,
        open var idStr: String? = null,
        open var isTranslator: Boolean? = null,
        open var lang: String? = null,
        open var listedCount: Int? = null,
        open var location: String? = null,
        open var name: String? = null,
        open var profileBackgroundColor: String? = null,
        open var profileBackgroundImageUrl: String? = null,
        open var profileBackgroundImageUrlHttps: String? = null,
        open var profileBackgroundTile: Boolean? = null,
        open var profileBannerUrl: String? = null,
        open var profileImageUrl: String? = null,
        open var profileImageUrlHttps: String? = null,
        open var profileLinkColor: String? = null,
        open var profileSidebarBorderColor: String? = null,
        open var profileSidebarFillColor: String? = null,
        open var profileTextColor: String? = null,
        open var profileUseBackgroundImage: Boolean? = null,
        open var protectedUser: Boolean? = null,
        open var screenName: String? = null,
        open var showAllInlineMedia: Boolean? = null,
        open var status: Tweet? = null,
        open var statusesCount: Int? = null,
        open var timeZone: String? = null,
        open var url: String? = null,
        open var utcOffset: Int? = null,
        open var verified: Boolean? = null,
        open var withheldInCountries: RealmList<RealmString> = RealmList(),
        open var withheldScope: String? = null
) : RealmObject()
