package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.fragment.ContatosFragment;
import com.example.whatsapp.fragment.ConversasFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);//para toolbar funcionar em versões anteriores do android

        //Configurar abas
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Conversas", ConversasFragment.class) //recomendação é usar strings.xml  //position 0
                .add("Contatos", ContatosFragment.class) //position 1
                .create()
        );
        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);

        //Configuração do search View
        searchView = findViewById(R.id.materialSearchPrincipal);

        //Listener para o search view
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            //quando é apresentado para o usuario (search view é aberto)
            public void onSearchViewShown() {

            }

            @Override
            //quando o search view é fechado
            public void onSearchViewClosed() {

                ConversasFragment fragment = (ConversasFragment)adapter.getPage(0);
                fragment.recarregarConversas();



            }
        });
        //Listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            //chamado quando usuario confirma a pesquisa
            public boolean onQueryTextSubmit(String query) {
                //Log.d("evento","onQueryTextSubmit");
                return false;
            }

            @Override
            //chamado quando abre a caixa de texto para pesquisa e altera o texto
            public boolean onQueryTextChange(String newText) {
                //Log.d("evento", "onQueryTextChange");

                //Verifica se está pesquisando Conversas ou Contatos
                //a partir da tab que esta ativa
                switch (viewPager.getCurrentItem()){
                    case 0:
                        ConversasFragment conversasFragment = (ConversasFragment)adapter.getPage(0);
                        if (newText != null && !newText.isEmpty() ){
                            conversasFragment.pesquisarConversas(newText.toLowerCase());//toLowerCase converte para letras minusculas
                        }
                        else {
                            conversasFragment.recarregarConversas();
                        }
                        break;
                    case 1:
                        ContatosFragment contatosFragment = (ContatosFragment)adapter.getPage(1);
                        if (newText != null && !newText.isEmpty() ){
                            contatosFragment.pesquisarContatos(newText.toLowerCase());//toLowerCase converte para letras minusculas
                        }
                        else {
                            contatosFragment.recarregarContatos();
                        }
                        break;

                }



                return true;
            }
        });


    }

    //criar menus para a toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //Configura botao de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    //recupera item de menu clicado
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarUsuario();
                finish();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario(){
        try {
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void abrirConfiguracoes(){
        Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
        startActivity(intent);
    }

}
