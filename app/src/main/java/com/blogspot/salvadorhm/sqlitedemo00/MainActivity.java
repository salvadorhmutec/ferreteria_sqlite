package com.blogspot.salvadorhm.sqlitedemo00;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    private EditText et_id_cliente;
    private EditText et_nombre;
    private EditText et_numero;
    private EditText et_ciudad;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_id_cliente = findViewById(R.id.et_id_cliente);
        et_nombre = findViewById(R.id.et_nombre);
        et_numero = findViewById(R.id.et_numero);
        et_ciudad = findViewById(R.id.et_ciudad);
        listaUsuarios();
    }

    // Damos de alta los usuarios en nuestra aplicación
    public void alta(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "ferreteria_acme", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String id_cliente = et_id_cliente.getText().toString();
        String nombre = et_nombre.getText().toString();
        String ciudad = et_ciudad.getText().toString();
        String numero = et_numero.getText().toString();

        ContentValues registro = new ContentValues();

        //registro.put("dni", dni);
        registro.put("nombre", nombre);
        registro.put("ciudad", ciudad);
        registro.put("numero", numero);

        // los inserto en la base de datos
        bd.insert("clientes", null, registro);
        //bd.close();

        // ponemos los campos a vacío para insertar el siguiente usuario
        et_id_cliente.setText("");
        et_nombre.setText("");
        et_numero.setText("");
        et_ciudad.setText("");

        Toast.makeText(this, "Datos del usuario cargados", Toast.LENGTH_SHORT).show();
        listaUsuarios();
    }

    // Hacemos búsqueda de usuario por DNI
    public void consulta(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "ferreteria_acme", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String dni = et_id_cliente.getText().toString();
        Cursor fila = bd.rawQuery(
                "select nombre, ciudad, numero from clientes where id_cliente=" + dni, null);
        if (fila.moveToFirst()) {
            et_nombre.setText(fila.getString(0));
            et_ciudad.setText(fila.getString(1));
            et_numero.setText(fila.getString(2));
        } else {
            et_id_cliente.setText("");
            et_nombre.setText("");
            et_numero.setText("");
            et_ciudad.setText("");
            Toast.makeText(this, "No existe ningún usuario con ese dni",Toast.LENGTH_SHORT).show();
        }
        //bd.close();
        listaUsuarios();
    }


    /* Método para dar de baja al usuario insertado*/
    public void baja(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "ferreteria_acme", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String dni = et_id_cliente.getText().toString();
        // aquí borro la base de datos del usuario por el dni
        int cant = bd.delete("clientes", "id_cliente=" + dni, null);
        //bd.close();
        et_id_cliente.setText("");
        et_nombre.setText("");
        et_ciudad.setText("");
        et_numero.setText("");
        if (cant == 1)
            Toast.makeText(this, "Usuario eliminado",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe usuario",Toast.LENGTH_SHORT).show();
        listaUsuarios();

    }

    // Método para modificar la información del usuario
    public void modificacion(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "ferreteria_acme", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String id_cliente = et_id_cliente.getText().toString();
        String nombre = et_nombre.getText().toString();
        String ciudad = et_ciudad.getText().toString();
        String numero = et_numero.getText().toString();

        ContentValues registro = new ContentValues();
        // actualizamos con los nuevos datos, la información cambiada
        registro.put("nombre", nombre);
        registro.put("ciudad", ciudad);
        registro.put("numero", numero);

        int cant = bd.update("clientes", registro, "id_cliente=" + id_cliente, null);
        if (cant == 1) {
            et_id_cliente.setText("");
            et_nombre.setText("");
            et_numero.setText("");
            et_ciudad.setText("");
            Toast.makeText(this,"Datos modificados con éxito",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"No existe usuario",Toast.LENGTH_SHORT).show();
        }
        //bd.close();
        listaUsuarios();
    }

    private void listaUsuarios() {
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                    "ferreteria_acme", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor fila = bd.rawQuery(
                    "select * from clientes", null);
            //bd.close();
            fila.moveToFirst();
           do {
                String id_cliente = fila.getString(0);
                String nombre = fila.getString(1);
                String ciudad = fila.getString(2);
                String numero =fila.getString(3);
                String registro = id_cliente +  " " +nombre + " " + ciudad + " " + numero;
                Log.i("Registros ", registro);
            } while (fila.moveToNext());
            //bd.close();
        } catch (Exception e) {
            Log.e("Error 100 ", e.getMessage());
        }
    }
}