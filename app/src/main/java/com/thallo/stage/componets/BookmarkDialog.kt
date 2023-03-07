package com.thallo.stage.componets

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.thallo.stage.R
import com.thallo.stage.database.bookmark.Bookmark
import com.thallo.stage.database.bookmark.BookmarkViewModel
import com.thallo.stage.database.shortcut.Shortcut
import com.thallo.stage.database.shortcut.ShortcutViewModel
import com.thallo.stage.databinding.DiaBookmarkBinding


class BookmarkDialog(
    var context1: Activity,
    title: String?,
    url: String?
) :
    MyDialog(context1) {
    var diaBookmarkBinding: DiaBookmarkBinding = DiaBookmarkBinding.inflate(LayoutInflater.from(context))
    var shortcutViewModel: ShortcutViewModel = ViewModelProvider(context1 as ViewModelStoreOwner).get<ShortcutViewModel>(ShortcutViewModel::class.java)
    var bookmarkViewModel: BookmarkViewModel = ViewModelProvider(context1 as ViewModelStoreOwner).get<BookmarkViewModel>(BookmarkViewModel::class.java)

    init {
        diaBookmarkBinding.textView5.setText(R.string.dia_add_bookmark_title)
        diaBookmarkBinding.diaBookmarkTitle.setText(title)
        diaBookmarkBinding.diaBookmarkUrl.setText(url)
        setView(diaBookmarkBinding.getRoot())
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.confirm),
            DialogInterface.OnClickListener { dialogInterface, i ->
                val bookmark = Bookmark(
                    diaBookmarkBinding.diaBookmarkUrl.getText().toString(),
                    diaBookmarkBinding.diaBookmarkTitle.getText().toString(),
                    "默认",
                    diaBookmarkBinding.radioButton.isChecked
                )
                val shortcut = Shortcut(
                    diaBookmarkBinding.diaBookmarkUrl.getText().toString(),
                    diaBookmarkBinding.diaBookmarkTitle.getText().toString(),
                    0
                )
                if (diaBookmarkBinding.radioButton.isChecked)
                    shortcutViewModel.insertShortcuts(shortcut)
                bookmarkViewModel.insertBookmarks(bookmark)
            })
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel),
            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
    }

    fun open() {
        super.show()
    }
}