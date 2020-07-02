package com.example.jack.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class buy_yours_changeaddress extends AppCompatActivity {

    //String Filename="address";
    private Spinner cityspr;
    private Spinner countyspr;
    private ArrayAdapter<String> cityadapter;
    private ArrayAdapter<String> countyadapter;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_yours_changeaddress);

        // 住家
        cityspr = (Spinner) findViewById(R.id.cityspinner);
        countyspr = (Spinner) findViewById(R.id.countyspinner);
        cityadapter = new ArrayAdapter<>(
                buy_yours_changeaddress.this, android.R.layout.simple_spinner_dropdown_item, city);
        countyadapter = new ArrayAdapter<>(
                buy_yours_changeaddress.this, android.R.layout.simple_spinner_dropdown_item, taipei);
        cityspr.setAdapter(cityadapter);
        countyspr.setAdapter(countyadapter);

        cityspr.setOnItemSelectedListener(selectListener);


        Button sbmbtn = (Button) findViewById(R.id.submitbtn);
        sbmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText postno = (EditText) findViewById(R.id.postno);
                Spinner city = (Spinner) findViewById(R.id.cityspinner);
                Spinner county = (Spinner) findViewById(R.id.countyspinner);
                EditText road = (EditText) findViewById(R.id.roadedt);

                String store = "";
                RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
                switch (rg.getCheckedRadioButtonId()) {
                    case R.id.radioButton1:
                        store = "7-11";
                        break;
                    case R.id.radioButton2:
                        store = "全家";
                        break;
                    case R.id.radioButton3:
                        store = "萊爾富";
                        break;
                }

                EditText shopname = (EditText) findViewById(R.id.shopname);
                EditText shopno = (EditText) findViewById(R.id.shopno);
                String haddress = city.getSelectedItem().toString() + county.getSelectedItem().toString() + road.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference address = database.getReference("address");
                buyeraddress datahome = new buyeraddress(haddress, postno.getText().toString());
                address.child(fuser.getUid()).child("homeaddress").setValue(datahome);

                buyeraddress datastore = new buyeraddress(shopname.getText().toString(), shopno.getText().toString(), store);
                address.child(fuser.getUid()).child("storeaddress").setValue(datastore);

                //利用Toast的靜態函式makeText來建立Toast物件
                Toast toast = Toast.makeText(buy_yours_changeaddress.this, "新增成功", Toast.LENGTH_LONG);
                //顯示Toast
                toast.show();

                Intent intent = new Intent();
                intent.setClass(buy_yours_changeaddress.this, buy_yours_addressdata.class);
                startActivity(intent);

            }
        });

    }

    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int pos = cityspr.getSelectedItemPosition();
            switch (pos) {
                case 0:
                    countyadapter = new ArrayAdapter<>(
                            buy_yours_changeaddress.this, android.R.layout.simple_spinner_dropdown_item, taipei);
                    countyspr.setAdapter(countyadapter);
                    break;
                case 1:
                    countyadapter = new ArrayAdapter<>(
                            buy_yours_changeaddress.this, android.R.layout.simple_spinner_dropdown_item, newtaipei);
                    countyspr.setAdapter(countyadapter);
                    break;
                case 2:
                    countyadapter = new ArrayAdapter<>(
                            buy_yours_changeaddress.this, android.R.layout.simple_spinner_dropdown_item, taoyuan);
                    countyspr.setAdapter(countyadapter);
                    break;
                case 3:
                    countyadapter = new ArrayAdapter<>(
                            buy_yours_changeaddress.this, android.R.layout.simple_spinner_dropdown_item, taichung);
                    countyspr.setAdapter(countyadapter);
                    break;
                case 4:
                    countyadapter = new ArrayAdapter<>(
                            buy_yours_changeaddress.this, android.R.layout.simple_spinner_dropdown_item, tainan);
                    countyspr.setAdapter(countyadapter);
                    break;
                case 5:
                    countyadapter = new ArrayAdapter<>(
                            buy_yours_changeaddress.this, android.R.layout.simple_spinner_dropdown_item, kaohsiung);
                    countyspr.setAdapter(countyadapter);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private String[] city = {"台北市", "新北市", "桃園市", "台中市", "台南市", "高雄市"};
    private String[] taipei = {"中正區", "大同區", "中山區", "松山區", "大安區", "萬華區", "信義區"
            , "士林區", "北投區", "內湖區", "南港區", "文山區"};
    private String[] newtaipei = {"板橋區", "中和區", "永和區", "土城區", "三峽區", "鶯歌區", "樹林區"
            , "新莊區", "三重區", "蘆洲區", "五股區", "泰山區", "林口區", "八里區", "淡水區", "三芝區"
            , "金山區", "萬里區", "汐止區", "瑞芳區", "貢寮區", "平溪區", "雙溪區", "新店區", "深坑區"
            , "石碇區", "坪林區", "烏來區"};
    private String[] taoyuan = {"桃園區", "八德區", "龜山區", "蘆竹區", "大園區", "大溪區", "中壢區"
            , "平鎮區", "楊梅區", "龍潭區", "新屋區", "觀音區", "復興區"};
    private String[] taichung = {"中區", "東區", "南區", "西區", "北區", "北屯區", "南屯區", "西屯區"
            , "太平區", "大里區", "霧峰區", "烏日區", "豐原區", "后里區", "石岡區", "東勢區", "和平區"
            , "新社區", "潭子區", "大雅區", "神岡區", "大肚區", "龍井區", "沙鹿區", "梧棲區", "清水區"
            , "大甲區", "外埔區", "大安區"};
    private String[] tainan = {"東區", "南區", "北區", "安南區", "安平區", "中西區", "新營區", "鹽水區"
            , "白河區", "柳營區", "後壁區", "東山區", "麻豆區", "下營區", "六甲區", "官田區", "大內區"
            , "佳里區", "學甲區", "西港區", "七股區", "將軍區", "北門區", "新化區", "善化區", "新市區"
            , "安定區", "山上區", "玉井區", "楠西區", "南化區", "左鎮區", "仁德區", "歸仁區", "關廟區"
            , "龍崎區", "永康區"};

    private String[] kaohsiung = {"鹽埕", "鼓山區", "左營區", "楠梓區", "三民區", "新興區", "前金區"
            , "苓雅區", "前鎮區", "旗津區", "小港區", "鳳山區", "林園區", "大寮區", "大樹區", "大社區"
            , "仁武區", "鳥松區", "岡山區", "橋頭區", "燕巢區", "田寮區", "阿蓮區", "路竹區", "湖內區"
            , "茄萣區", "永安區", "彌陀區", "梓官區", "旗山區", "美濃區", "六龜區", "甲仙區", "杉林區"
            , "內門", "茂林區", "桃源區", "那瑪夏區"};

}
