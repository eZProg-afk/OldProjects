package spiral.bit.dev.lgbtswipe.views.msgs_views_holders

import spiral.bit.dev.lgbtswipe.views.msgs_views.MessageView

interface MessageHolder {
    fun drawMsg(view: MessageView)
    fun onAttach(view: MessageView)
    fun onDetach()
}