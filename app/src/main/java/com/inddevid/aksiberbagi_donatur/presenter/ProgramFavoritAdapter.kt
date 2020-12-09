package com.inddevid.aksiberbagi_donatur.presenter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.androchef.happytimer.utils.extensions.invisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramAll
import com.inddevid.aksiberbagi_donatur.services.Preferences
import com.inddevid.aksiberbagi_donatur.view.ProgramDetailActivity
import kotlinx.android.synthetic.main.beranda_card_program_all.view.fundProgramAll
import kotlinx.android.synthetic.main.beranda_card_program_all.view.imageProgramAll
import kotlinx.android.synthetic.main.beranda_card_program_all.view.programAllLineList
import kotlinx.android.synthetic.main.beranda_card_program_all.view.progressBar
import kotlinx.android.synthetic.main.beranda_card_program_all.view.titleProgramAll
import kotlinx.android.synthetic.main.beranda_card_program_all.view.volunteerProgamAll
import kotlinx.android.synthetic.main.card_horizontal_program.view.*

class ProgramFavoritAdapter(val arrayList: ArrayList<BerandaProgramAll>, val context: Context) :
    RecyclerView.Adapter<ProgramFavoritAdapter.ViewHolder> (){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .override(220, 175)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        fun bindItems(model: BerandaProgramAll){
            Glide.with(itemView.imageProgramAll.context).load(model.img).apply(options).into(itemView.imageProgramAll)
            itemView.titleProgramAll.text = model.title
            itemView.volunteerProgamAll.text = model.volunteer
            itemView.progressBar.progress = model.progresProgram!!
            val donasi = model.fund
            itemView.fundProgramAll.text = "Rp $donasi"
            itemView.btnFavoritReplace.bringToFront()
//            itemView.dayProgramAll.text = model.dayFund
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProgramFavoritAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_horizontal_program, parent, false)
        return ProgramFavoritAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ProgramFavoritAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        if (position == 4){
            holder.itemView.programAllLineList.invisible()
        }

        val sharedPreference: Preferences = Preferences(holder.itemView.context)
        holder.itemView.setOnClickListener {
            val model = arrayList[position]
            sharedPreference.save("idProgram", model.id)
            sharedPreference.save("img", model.img)
            sharedPreference.save("judul", model.title)
            sharedPreference.save("capaian", model.fund.toString())
            sharedPreference.save("sisaHari", model.dayFund)
            sharedPreference.save("tanggalMulai", model.startFund)
            sharedPreference.save("progressProgram", model.progresProgram)
            sharedPreference.save("targetProgram", model.targetNominal)
            sharedPreference.save("navigasi", model.poinAkses)
            val mIntent = Intent(context, ProgramDetailActivity::class.java)
            context.startActivity(mIntent)
        }

        holder.itemView.setOnLongClickListener {
            val model = arrayList[position]
            dialogPanel(model.id.toString(), holder.itemView.context)
            holder.itemView.context.vibratePhone()
            return@setOnLongClickListener true
        }
    }

    private fun dialogPanel(id: String, context: Context){
        var dialog = DialogFavoritSaya(id)
        val fragment = getFragmentManager(context)
        fragment!!.let { dialog.show(it, "dialogBatal") }
    }

    fun getFragmentManager(context: Context?): FragmentManager? {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> getFragmentManager(context.baseContext)
            else -> null
        }
    }

    fun Context.vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

}