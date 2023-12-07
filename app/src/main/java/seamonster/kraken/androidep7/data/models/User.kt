package seamonster.kraken.androidep7.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import java.io.Serializable
import java.util.Calendar

class User() : BaseObservable(), Serializable {

    @get:Bindable
    var active: Boolean? = true
        set(value) { field = value;notifyPropertyChanged(BR.active) }

    @get:Bindable
    var birthPlace: String? = null
        set(value) { field = value;notifyPropertyChanged(BR.birthPlace) }

    @get:Bindable
    var changePass: Boolean? = true
        set(value) { field = value;notifyPropertyChanged(BR.changePass) }

    @get:Bindable
    var confirmPassword: String? = null
        set(value) { field = value;notifyPropertyChanged(BR.confirmPassword) }

    @get:Bindable
    var countDayCheckin: Int? = null
        set(value) { field = value;notifyPropertyChanged(BR.countDayCheckin) }

    @get:Bindable
    var countDayTracking: Int? = null
        set(value) { field = value;notifyPropertyChanged(BR.countDayTracking) }

    @get:Bindable
    var displayName: String? = null
        set(value) { field = value;notifyPropertyChanged(BR.displayName) }

    @get:Bindable
    var dob: Calendar? = null
        set(value) { field = value; notifyPropertyChanged(BR.dob) }

    @get:Bindable
    var email: String? = null
        set(value) { field = value; notifyPropertyChanged(BR.email) }

    @get:Bindable
    var firstName: String? = null
        set(value) { field = value; notifyPropertyChanged(BR.firstName) }

    @get:Bindable
    var gender: String? = null
        set(value) { field = value; notifyPropertyChanged(BR.gender) }

    @get:Bindable
    var hasPhoto: Boolean? = false
        set(value) { field = value; notifyPropertyChanged(BR.hasPhoto) }

    @get:Bindable
    var id: Int? = null
        set(value) { field = value; notifyPropertyChanged(BR.id) }

    @get:Bindable
    var image: String? = null
        set(value) { field = value; notifyPropertyChanged(BR.image) }

    @get:Bindable
    var lastName: String? = null
        set(value) { field = value; notifyPropertyChanged(BR.lastName) }

    @get:Bindable
    var password: String? = null
        set(value) { field = value; notifyPropertyChanged(BR.password) }

    @get:Bindable
    var roles: List<Role>? = null
        set(value) { field = value; notifyPropertyChanged(BR.roles) }

    @get:Bindable
    var setPassword: Boolean? = false
        set(value) { field = value; notifyPropertyChanged(BR.setPassword) }

    @get:Bindable
    var tokenDevice: String? = null
        set(value) { field = value; notifyPropertyChanged(BR.tokenDevice) }

    @get:Bindable
    var university: String? = null
        set(value) { field = value; notifyPropertyChanged(BR.university) }

    @get:Bindable
    var username: String? = null
        set(value) { field = value; notifyPropertyChanged(BR.username) }

    @get:Bindable
    var year: Int? = null
        set(value) { field = value; notifyPropertyChanged(BR.year) }

}

fun User.isAdmin(): Boolean {
    val joined = this.roles?.joinToString(";") { it.name ?: "" }
    return joined?.contains("ADMIN") ?: false
}

fun User?.clone(): User? {
    if (this == null) return null
    val newUser = User()
    User::class.java.declaredFields.forEach { field ->
        val modifier = field.isAccessible // save accessible state
        field.isAccessible = true // make field accessible
        field.set(newUser, field.get(this)) // set current field to new object
        field.isAccessible = modifier // restore accessible state
    }
    return newUser
}

fun User?.toFilteredUser(): User? {
    if (this == null) return null
    return User().apply {
        val ref = this@toFilteredUser // reference to top most scope
        id = ref.id
        active = ref.active
        changePass = ref.changePass
        username = ref.username
        displayName = ref.displayName
        email = ref.email
        firstName = ref.firstName
        lastName = ref.lastName
        gender = ref.gender
        dob = ref.dob
        birthPlace = ref.birthPlace
        university = ref.university
        year = ref.year
        roles = ref.roles
        image = ref.image
    }
}
