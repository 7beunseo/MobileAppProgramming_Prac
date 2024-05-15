package com.example.myapplication10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication10.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    lateinit var binding: ActivityMainBinding
    // DrawerLayout Toggle
    lateinit var toggle: ActionBarDrawerToggle

    lateinit var headerView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 0
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener{
            val name = binding.edtName.text.toString()
            Log.d("mobileapp", name)

            val call: Call<XmlResponse> = RetrofitConnection.xmlNetworkService.getXmlList(
                name,
                1,
                10,
                "xml",
                "APKTrp0XMZTlReSionHVfAbVsgefp6rmsviSNGmE5MndTP43LqhqvSm2n7Qj+2GQ3TpsgbH/KaUWDEMV5ApISg=="
            )
            call?.enqueue(object : Callback<XmlResponse> {
                override fun onResponse(call: Call<XmlResponse>, response: Response<XmlResponse>) {
                    if(response.isSuccessful){
                        Log.d("mobileApp", "$response")
                        Log.d("mobileApp", "${response.body()}")
                        binding.xmlRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        binding.xmlRecyclerView.adapter = XmlAdapter(response.body()!!.body!!.items!!.item)
                        binding.xmlRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
                    }
                }

                override fun onFailure(call: Call<XmlResponse>, t: Throwable) {
                    Log.d("mobileApp", "onFailure ${call.request()}")
                }
            })
        }

// DrawerLayout Toggle
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            R.string.drawer_opened,
            R.string.drawer_closed
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // Drawer 메뉴
        binding.mainDrawerView.setNavigationItemSelectedListener(this)

        headerView = binding.mainDrawerView.getHeaderView(0)
        val button = headerView.findViewById<Button>(R.id.btnAuth)
        button.setOnClickListener {
            Log.d("mobileapp", "button.setOnClickListener")
            val intent = Intent(this, AuthActivity::class.java)

            if(button.text.equals("로그인"))  {
                intent.putExtra("status", "logout")
            } else if(button.text.equals("로그아웃")) {
                intent.putExtra("status", "login")
            }
            startActivity(intent)

            binding.drawer.closeDrawers()
        }

        // 본 화면 기능 작성
        binding.btnSearch.setOnClickListener {
        }
    }

    // DrawerLayout Toggle
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    // Drawer 메뉴
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_setting -> {
                Log.d("mobileapp", "설정 메뉴")
                binding.drawer.closeDrawers()
                true
            }
        }
        return false
    }

    // 다른 엑티비티로 갔다가 돌아옴
    override fun onStart() {
        super.onStart()

        // 처리
        val button = headerView.findViewById<Button>(R.id.btnAuth)
        val tv = headerView.findViewById<TextView>(R.id.tvID)

        if(MyApplication.checkAuth()) {
            button.text = "로그아웃"
            tv.text = "${MyApplication.email}님\n반갑습니다"
        } else {
            button.text = "로그인"
            tv.text = "안녕하세요"
        }
    }
}