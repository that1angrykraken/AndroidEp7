package seamonster.kraken.androidep7.ui.entry

sealed class EntryAction {
    data object GetCurrentUser: EntryAction()
}