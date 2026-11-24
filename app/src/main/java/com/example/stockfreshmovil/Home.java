package com.example.stockfreshmovil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {


    private static final int menu_inicio = R.id.inicio;
    private static final int menu_bodega = R.id.bodegaV;
    private static final int menu_estatus = R.id.estatusP;
    private static final int menu_listas = R.id.listas;
    private static final int menu_recetas = R.id.recetasP;
    private static final int menu_consumo = R.id.consumo;
    private static final int menu_menus = R.id.menus;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);



        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets sb = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), sb.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Stock & Fresh");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        ViewCompat.setOnApplyWindowInsetsListener(headerView, (v, insets) -> {
            Insets sb = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), sb.top + dpToPx(16),
                    v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        navigationView.setNavigationItemSelectedListener(this);
        pintarSeleccion(navigationView, R.id.inicio);
        navigationView.setItemIconTintList(null);

        findViewById(R.id.btn_perfil_footer).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new perfilfragment())
                    .commit();
        });

        TextView tvNombre = headerView.findViewById(R.id.tvNombreUsuario);
        TextView tvCorreo = headerView.findViewById(R.id.tvCorreoUsuario);

        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        tvNombre.setText(prefs.getString("nombre", "Nombre Usuario"));
        tvCorreo.setText(prefs.getString("correoElectronico", "correoUsuario@stockFresh.com"));

        //Busca el mennu
        Menu menu = navigationView.getMenu();
        //Cambia el color de los titulos que estan en el menu
        setMenuItemTitleColor(menu.findItem(R.id.inicio), "Inicio", "#da6220");   // Anaranjado /
        setMenuItemTitleColor(menu.findItem(R.id.bodegaV), "Bodega de Viveres", "#E5A93C");   // Amarillo /
        setMenuItemTitleColor(menu.findItem(R.id.estatusP), "Estatus de Productos", "#C54B54"); // Rojo /
        setMenuItemTitleColor(menu.findItem(R.id.listas), "Listas", "#87C040");               // Verde
        setMenuItemTitleColor(menu.findItem(R.id.recetasP), "Recetas Propias", "#4B83E5");      // Azul
        setMenuItemTitleColor(menu.findItem(R.id.consumo), "Consumo", "#9A4CE3");              // Morado
        setMenuItemTitleColor(menu.findItem(R.id.menus), "Menús", "#D63D3D");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new fragmentHome()).commit();
        }
    }

    private void pintarSeleccion(NavigationView nav, int itemId) {
        int color;
        if (itemId == R.id.inicio)       color = R.color.sel_inicio;
        else if (itemId == R.id.bodegaV)  color = R.color.sel_bodega;
        else if (itemId == R.id.estatusP) color = R.color.sel_estatus;
        else if (itemId == R.id.listas)  color = R.color.sel_listas;
        else if (itemId == R.id.recetasP) color = R.color.sel_recetas;
        else if (itemId == R.id.consumo) color = R.color.sel_consumo;
        else                                 color = R.color.sel_menus;

        android.graphics.drawable.GradientDrawable seleccionado =
                new android.graphics.drawable.GradientDrawable();
        seleccionado.setColor(androidx.core.content.ContextCompat.getColor(this, color));
        seleccionado.setCornerRadius(100f * getResources().getDisplayMetrics().density);

        android.graphics.drawable.StateListDrawable fondo =
                new android.graphics.drawable.StateListDrawable();
        fondo.addState(new int[]{android.R.attr.state_checked}, seleccionado);
        fondo.addState(new int[]{},
                new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

        nav.setItemBackground(fondo);
    }
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    //Metodo para cambiar el color de los titulos en el menu
    private void setMenuItemTitleColor(MenuItem item, String title, String colorHex) {
        if (item != null) {
            String htmlText = "<font color='" + colorHex + "'><b>" + title + "</b></font>";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                item.setTitle(android.text.Html.fromHtml(htmlText, android.text.Html.FROM_HTML_MODE_LEGACY));
            } else {
                item.setTitle(android.text.Html.fromHtml(htmlText));
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        drawerLayout.closeDrawer(GravityCompat.START);
        if (itemId == menu_inicio) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new fragmentHome()).commit();
        } else if (itemId == menu_bodega) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new bodegaviveresfragment()).commit();
        } else if (itemId == menu_estatus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new estatusProductosfragment()).commit();
        } else if (itemId == menu_listas) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new listasfragment()).commit();
        } else if (itemId == menu_recetas) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new recetasPropiasfragment()).commit();
        } else if (itemId == menu_consumo) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new consumofragment()).commit();
        } else if (itemId == menu_menus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new menusfragment()).commit();
        }

        pintarSeleccion(navigationView, itemId);

        return true;
    }
}