package com.aksihijau.ui.navigationmenu.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.R
import com.aksihijau.databinding.FragmentProfileBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.aksihijau.ui.makecampaign.HomeMakeCampaignActivity
import com.aksihijau.ui.fiturcampaign.mycampaigns.MyCampaignsActivity
import com.aksihijau.ui.user.login.LoginActivity
import com.aksihijau.ui.view.SplashScreen
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val pref = TokenPreferences.getInstance(requireContext().dataStore)
        tokenViewModel = ViewModelProvider(this, TokenViewModelFactory(pref))[TokenViewModel::class.java]

//        val textView: TextView = binding.textProfile
        profileViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = itx
        }


        tokenViewModel.getLoginSettings().observe(viewLifecycleOwner){
            if(!it){
                binding.btLogin2.visibility = View.VISIBLE
                binding.textView14.visibility = View.VISIBLE

                binding.btLogin2.setOnClickListener {
                    val intent = Intent( requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }

                binding.imageCompany.visibility =       View.GONE
                binding.tvNamaProfile.visibility =      View.GONE
                binding.cvAdmin.visibility =     View.GONE
                binding.cvDatapribadi.visibility =      View.GONE
                binding.cvLogoutProfile.visibility =    View.GONE
                binding.cvKampanyasaya.visibility =     View.GONE

            }else {
                binding.btLogin2.visibility = View.INVISIBLE
                binding.textView14.visibility = View.INVISIBLE

                binding.imageCompany.visibility =       View.VISIBLE
                binding.tvNamaProfile.visibility =      View.VISIBLE
                binding.cvAdmin.visibility =     View.VISIBLE
                binding.cvDatapribadi.visibility =      View.VISIBLE
                binding.cvLogoutProfile.visibility =    View.VISIBLE
                binding.cvKampanyasaya.visibility =     View.VISIBLE

                tokenViewModel.getToken().observe(viewLifecycleOwner){ token->
                    tokenViewModel.getRefreshToken().observe(viewLifecycleOwner){refreshToken->
                        profileViewModel.getUser(token, refreshToken)
                    }
                }

                setupView()
                profileViewModel.user.observe(viewLifecycleOwner){
                    Log.d("Profie Fragment", "onCreateView: $it")

                    val imageUrl = it!!.photo.toString().replace("storage.cloud.google.com", "storage.googleapis.com")

                    Glide.with(binding.root).load(imageUrl).apply(
                        RequestOptions().format(DecodeFormat.PREFER_RGB_565)) .error(R.drawable.aksihijau_logo).into(binding.imageCompany)
                    binding.tvNamaProfile.text = it.name
                    if(it.role == "user"){
                        binding.cvAdmin.visibility = View.GONE
                    }
                    binding.cvDatapribadi.setOnClickListener {view->
                        val intent = Intent(requireContext(), UpdateUserActivity::class.java)

                        intent.putExtra(UpdateUserActivity.EXTRA_NAME, it.name)
                        intent.putExtra(UpdateUserActivity.EXTRA_EMAIL, it.email)
                        startActivity(intent)
                    }
                }
            }
        }

        binding.cvAdmin.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://aksihijau-admin.vercel.app"))
            startActivity(browserIntent)
        }


        return root
    }

    private fun openSoilAnalysisActivity() {
        val intent = Intent(requireContext(), HomeMakeCampaignActivity::class.java)
        startActivity(intent)
    }

    private fun setupView(){

        binding.cvLogoutProfile.setOnClickListener{
            tokenViewModel.saveLoginSetting(false)
            tokenViewModel.saveToken("")
            tokenViewModel.saveRefreshToken("")

            tokenViewModel.getLoginSettings().observe(viewLifecycleOwner){
                if(!it){
                    val intent = Intent(requireContext(), SplashScreen::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        binding.cvKampanyasaya.setOnClickListener {
            startActivity(Intent(requireContext(), MyCampaignsActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}