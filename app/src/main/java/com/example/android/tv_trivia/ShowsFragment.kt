package com.example.android.tv_trivia

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.tv_trivia.databinding.FragmentShowsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentShowsBinding>(inflater,
                R.layout.fragment_shows,container,false)
        binding.apply {
            moneyHeist.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_showsFragment_to_moneyFragment)
            }
            brooklyn.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_showsFragment_to_brooklynFragment)
            }
            friends.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_showsFragment_to_friendsFragment)
            }
            sherlock.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_showsFragment_to_sherlockFragment)
            }
            moneyText.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_showsFragment_to_moneyFragment)
            }
            brooklynText.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_showsFragment_to_brooklynFragment)
            }
            friendsText.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_showsFragment_to_friendsFragment)
            }
            sherlockText.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_showsFragment_to_sherlockFragment)
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
                view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShowsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ShowsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}