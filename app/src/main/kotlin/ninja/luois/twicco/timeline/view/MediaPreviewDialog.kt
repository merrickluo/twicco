package ninja.luois.twicco.timeline.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotterknife.bindView
import ninja.luois.twicco.R
import ninja.luois.twicco.common.DialogFragment


class MediaPreviewDialog(val medias: List<Media>, val position: Int) : DialogFragment() {

    val counterView: TextView by bindView(R.id.text_media_counter)
    val pager: ViewPager by bindView(R.id.view_pager_media)

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.dialog_media_preview, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // the content
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        root.setBackgroundColor(Color.TRANSPARENT)

        // creating the fullscreen dialog
        val dialog = Dialog(activity)
        dialog.setContentView(root)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.adapter = MediaViewPagerAdapter(activity, medias) {
            dismiss()
        }
        if (medias.size > 1) {
            counterView.text = "1 of ${medias.size}"
        }
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                counterView.text = "${position+1} of ${medias.size}"
            }

        })
        pager.setCurrentItem(position)
    }
}

data class Media(val url: String)

class MediaViewPagerAdapter(val ctx: Context,
                            val medias: List<Media>,
                            val clickAction: () -> Unit) : PagerAdapter() {

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object` as ImageView
    }

    override fun getCount(): Int {
        return medias.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {

        val padding = ctx.resources.getDimensionPixelSize(
                R.dimen.padding_medium)

        val imageView = ImageView(ctx)
        imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

        imageView.setPadding(padding, padding, padding, padding)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.setBackgroundColor(Color.TRANSPARENT)

        val m = medias[position]
        Picasso.with(ctx)
                .load("${m.url}:large")
                .into(imageView)

        imageView.setOnClickListener { clickAction() }

        (container as ViewPager).addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        (container as? ViewPager)?.removeView(`object` as ImageView)
    }
}
