package hardcoder.dev.stubmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private val navHost by lazy {
        supportFragmentManager.findFragmentById(R.id.mainFragmentContainerView) as NavHostFragment
    }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = navHost.navController
        navController.navigate(R.id.calculatorStubFragment)
    }
}