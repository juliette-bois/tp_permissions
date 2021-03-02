package fr.juliettebois.dmii.tp_permissions

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    lateinit var buttonApp: Button
    lateinit var textApp: TextView
    private val permissionRequestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonApp = findViewById(R.id.button_app)
        textApp = findViewById(R.id.text_app)

        buttonApp.setOnClickListener(
            View.OnClickListener {
                checkPermission()
            }
        )
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Afficher une popup pour expliquer à l'utilisateur pourquoi on a besoin de son autorisation
                textApp.text = "Vous devez autoriser l'accès au GPS pour qu'on puisse récupérer votre position"
                onCreateDialog()
            } else {
                // Demander la permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionRequestCode)
            }
        } else {
            // Continuer car la permission est accordée
            textApp.text = "Bienvenue sur notre application, pour avoir votre position cliquer sur le bouton 'Autoriser l'accès'"
        }
    }

    private fun onCreateDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Autorisation")
            .setMessage("Nous avons besoin de votre accord pour afficher votre position GPS")
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialog, id ->
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), permissionRequestCode)
                    Toast.makeText(applicationContext, "Merci d'avoir accepté", Toast.LENGTH_SHORT).show()
                }
            )
            .setNegativeButton(
                "Annuler",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                    Toast.makeText(applicationContext, "Vous n'êtes pas d'accord", Toast.LENGTH_SHORT).show()
                }
            )
        builder.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            permissionRequestCode -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    textApp.text = "Bienvenue sur notre application, pour avoir votre position cliquer sur le bouton 'Autoriser l'accès'"
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    textApp.text = "Vous devez autoriser l'accès au GPS pour qu'on puisse récupérer votre position"
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
