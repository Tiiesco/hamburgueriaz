package com.unopar.hamburgueriaz;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNome;
    private CheckBox checkBoxBacon, checkBoxQueijo, checkBoxOnionRings;
    private TextView textViewQuantidade, textViewResumo;
    private Button btnIncrementar, btnDecrementar, btnEnviar;

    private int quantidade = 0;
    private final double PRECO_BASE = 20.0;
    private final double PRECO_BACON = 2.0;
    private final double PRECO_QUEIJO = 2.0;
    private final double PRECO_ONION_RINGS = 3.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNome = findViewById(R.id.editTextNome);
        checkBoxBacon = findViewById(R.id.checkBoxBacon);
        checkBoxQueijo = findViewById(R.id.checkBoxQueijo);
        checkBoxOnionRings = findViewById(R.id.checkBoxOnionRings);
        textViewQuantidade = findViewById(R.id.textViewQuantidade);
        textViewResumo = findViewById(R.id.textViewResumo);
        btnIncrementar = findViewById(R.id.btnIncrementar);
        btnDecrementar = findViewById(R.id.btnDecrementar);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnIncrementar.setOnClickListener(v -> somar());
        btnDecrementar.setOnClickListener(v -> subtrair());
        btnEnviar.setOnClickListener(v -> enviarPedido());
    }

    private void somar() {
        quantidade++;
        atualizarQuantidade();
    }

    private void subtrair() {
        if (quantidade > 0) {
            quantidade--;
            atualizarQuantidade();
        }
    }

    private void atualizarQuantidade() {
        textViewQuantidade.setText(String.valueOf(quantidade));
    }

    private void enviarPedido() {
        String nome = editTextNome.getText().toString().trim();

        if (nome.isEmpty()) {
            Toast.makeText(this, "Por favor, digite seu nome", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantidade == 0) {
            Toast.makeText(this, "Por favor, selecione pelo menos 1 hambúrguer", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean temBacon = checkBoxBacon.isChecked();
        boolean temQueijo = checkBoxQueijo.isChecked();
        boolean temOnionRings = checkBoxOnionRings.isChecked();

        double precoTotal = calcularPrecoTotal(temBacon, temQueijo, temOnionRings);

        String resumo = gerarResumoPedido(nome, temBacon, temQueijo, temOnionRings, precoTotal);
        textViewResumo.setText(resumo);

        enviarEmail(nome, resumo);
    }

    private double calcularPrecoTotal(boolean temBacon, boolean temQueijo, boolean temOnionRings) {
        double precoAdicionais = 0;

        if (temBacon) precoAdicionais += PRECO_BACON;
        if (temQueijo) precoAdicionais += PRECO_QUEIJO;
        if (temOnionRings) precoAdicionais += PRECO_ONION_RINGS;

        return quantidade * (PRECO_BASE + precoAdicionais);
    }

    private String gerarResumoPedido(String nome, boolean temBacon, boolean temQueijo, boolean temOnionRings, double precoTotal) {
        return "Nome do cliente: " + nome + "\n" +
                "Tem Bacon? " + (temBacon ? "Sim" : "Não") + "\n" +
                "Tem Queijo? " + (temQueijo ? "Sim" : "Não") + "\n" +
                "Tem Onion Rings? " + (temOnionRings ? "Sim" : "Não") + "\n" +
                "Quantidade: " + quantidade + "\n" +
                "Preço final: R$ " + String.format(Locale.getDefault(),"%.2f", precoTotal);
    }

    private void enviarEmail(String nome, String resumo) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nome);
        intent.putExtra(Intent.EXTRA_TEXT, resumo);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}