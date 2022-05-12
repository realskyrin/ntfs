package cn.skyrin.common.view

import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.core.view.descendants
import androidx.core.view.forEach
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

fun ChipGroup.getChipsValues(@IdRes except: Int, @IdRes emptyView: Int = 0): List<String> {
    val list = mutableListOf<String>()
    forEach {
        (it as? Chip)?.let { chip ->
            if (chip.id == except || chip.id == emptyView) return@forEach
            list.add(chip.text.toString())
        }
    }
    return list
}

fun ChipGroup.getChildChips(@IdRes except: Int, @IdRes emptyView: Int = 0): List<Chip> {
    val list = mutableListOf<Chip>()
    forEach {
        (it as? Chip)?.let { chip ->
            if (chip.id == except || chip.id == emptyView) return@forEach
            list.add(chip)
        }
    }
    return list
}

fun Toolbar.setOnMenuItemDebouncedClickListener(
    delayBetweenClicks: Long = DEFAULT_DEBOUNCE_INTERVAL,
    block: (MenuItem) -> Unit,
) {
    setOnMenuItemClickListener(object : DebouncedMenuItemClickListener(delayBetweenClicks) {
        override fun onDebouncedMenuItemClick(item: MenuItem) = block(item)
    })
}

fun BottomNavigationView.setOnMenuItemDebouncedClickListener(
    delayBetweenClicks: Long = DEFAULT_DEBOUNCE_INTERVAL,
    block: (MenuItem) -> Unit,
) {
    setOnItemSelectedListener(object : DebouncedMenuItemClickListener(delayBetweenClicks) {
        override fun onDebouncedMenuItemClick(item: MenuItem) = block(item)
    })
}

val ViewGroup.visibleDescendants: Sequence<View?>
    get() = sequence {
        forEach { child ->
            if (!child.isVisible) return@forEach
            if (child is ViewGroup) {
                yieldAll(child.visibleDescendants)
            } else {
                yield(child)
            }
        }
    }

val ViewGroup.visibleTextViews: Sequence<TextView?>
    get() = visibleDescendants.filterIsInstance<TextView>()

fun ViewGroup.findViewByText(text: CharSequence): View? {
    return visibleDescendants.filter { (it as? TextView)?.text == text }.takeIf { it.count() > 0 }
        ?.first() ?: findViewWithText(text)
}

fun ViewGroup.findViewWithText(text: CharSequence): View? {
    val outViews = arrayListOf<View>()
    findViewsWithText(outViews, text, View.FIND_VIEWS_WITH_TEXT)
    return outViews.takeIf { it.isNotEmpty() }?.first()
}