package spiral.bit.dev.lgbtswipe.other

import android.Manifest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import spiral.bit.dev.lgbtswipe.models.User

const val MAIN_PREFS_NAME = "main_prefs"
const val PREFS_MODE = 0
lateinit var REFERENCE_DATABASE: DatabaseReference

//Database

lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_USER_ID: String
lateinit var RECEIVING_USER_ID: String
lateinit var REFERENCE_STORAGE: StorageReference
lateinit var USER: User
const val TYPE_TEXT = "text"

//NODE'S:

const val NODE_USERS = "users_swipe"
const val NODE_MESSAGES = "messages_swipe"
const val NODE_GENDERS = "genders_swipe"
const val NODE_COUNTRIES = "countries_swipe"
const val NODE_CHAT_LIST = "chat_list_swipe"
const val NODE_SYMPATHIES = "user_sympathies"

const val AVATARS_USERS = "avatars_swipe"
const val FILES_USERS = "files_swipe"

//CHILD'S:

const val CHILD_ID = "id"
const val CHILD_EMAIL = "email"
const val CHILD_PASSWORD = "password"
const val CHILD_PHONE = "phone"
const val CHILD_BIRTH_DATE = "birth_date"
const val CHILD_NAME = "name"
const val CHILD_GENDER_MOON = "gender"
const val CHILD_PHOTO_URL_AVATAR = "photo_url"
const val CHILD_STATUS = "status"
const val CHILD_TIME_STAMP = "time_stamp"
const val CHILD_SYMPATHIES = "sympathies"
const val CHILD_STATE = "state"

const val CHILD_ABOUT_MOON = "about"
const val CHILD_CLOSED_PROFILE = "closed_profile_moon"
const val CHILD_TEXT_MOON = "text_moon"
const val CHILD_TYPE_MOON = "type_moon"
const val CHILD_FROM_MOON = "from_moon"
const val CHILD_TIME_STAMP_MOON = "time_stamp_moon"
const val CHILD_FILE_URL_MOON = "file_url_moon"
const val CHILD_IS_MSG_CHECKED = "is_msg_checked"
const val CHILD_TRACK_NAME_MOON = "track_name_moon"
const val CHILD_TRACK_AUTHOR_MOON = "author_of_track_moon"
const val AUTH_UI_REQUEST_CODE = 10001

//GENDER'S:

const val F2M = "F2M"
const val M2F = "M2F"
const val AGENDER = "agender"
const val ANDROGIN = "androgin"
const val ANDROGIN_PERSON = "androgin_person"
const val WITHOUT_GENDER = "without_gender"
const val BIGENDER = "bigender"
const val IN_SEARCH_GENDER = "in_search_gender"
const val GENDERQUIR = "gender_quir"
const val GENDERFLUID = "genderfluid"
const val OTHER = "other_gender"
const val GM = "G-M"
const val GM_TRANS = "G-M-trans"
const val GM_TRANS_GENDER = "G-M-trans_gender"
const val GM_TRANS_SEX = "G-M-trans-sex"
const val G_TRANS = "woman_trans"
const val G_TRANS_GENDER = "woman_trans_gender"
const val FROM_G_TO_M = "from_wom_to_man"
const val FROM_M_TO_G = "from_man_to_wom"
const val MG = "M-G"
const val MG_TRANS = "M-G-trans"
const val MG_TRANS_GENDER = "M-G-trans_gender"
const val MG_TRANS_SEX = "M-G-trans_sex"
const val M_TRANS = "man_trans"
const val M_TRANS_GENDER = "man_trans_gender"
const val UNBINARY_GENDER = "unbinary_gender"
const val PANGENDER = "pangender"
const val POLIGENDER = "poligender"
const val TRANS = "trans"
const val TRANS_GENDER = "trans_gender"
const val TRANS_FEMININ = "trans_feminin"
const val TRANS_SEXUAL = "trans_sexual"
const val TWO_SPIRIT = "two_spirit"
const val CISGENDER = "cis_gender"
const val ENBY = "enby"

//APP CONSTANTS

const val TYPE_MSG_IMG = "image"
const val TYPE_MSG_TXT = "text"
const val TYPE_MSG_VOICE ="voice"
const val TYPE_MSG_FILE = "file"
const val TYPE_MSG_CONTACT = "contact"
const val TYPE_MSG_GIF = "gif"
const val TYPE_MSG_VIDEO = "video"
const val TYPE_MSG_LOCATION = "location"

const val TYPE_CHAT ="chat"
const val TYPE_GROUP ="group"
const val TYPE_CHANNEL = "channel"

const val PICK_FILE_REQUEST_CODE = 413
const val PICK_GIF_REQUEST_CODE = 414
const val PICK_VIDEO_REQUEST_CODE = 415
const val SHARE_PHOTO_REQUEST_CODE = 416
const val PICK_AVA_REQUEST_CODE = 417
const val PICK_CONTACT_CODE = 418
const val REQUEST_CODE_SPEECH = 4110
const val REQUEST_CODE_SET_DEFAULT_DIALER = 419
const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO