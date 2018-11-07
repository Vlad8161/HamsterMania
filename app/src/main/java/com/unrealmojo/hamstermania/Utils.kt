package com.unrealmojo.hamstermania

import android.content.Context
import android.content.Intent
import com.unrealmojo.hamstermania.data.ui.Hamster

fun shareHamster(context: Context, hamster: Hamster) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "text/plain"
    intent.putExtra(
            Intent.EXTRA_TEXT, """
${hamster.title}
${hamster.description}
${hamster.image}
            """
    )
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.title_share_app_chooser)))
}