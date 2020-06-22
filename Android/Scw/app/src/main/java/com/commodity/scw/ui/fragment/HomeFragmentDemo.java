package com.commodity.scw.ui.fragment;

import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.ui.BaseFragment;
import com.commodity.scw.ui.activity.CeshiActivity;
import com.commodity.scw.ui.demo.SqlLiteActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：liyushen on 2017/03/13 11:13
 * 功能：无
 */
public class HomeFragmentDemo extends BaseFragment {
    @Bind(R.id.eeee)
    Button eeee;
    @Bind(R.id.netquest)
    Button netquest;
    @Bind(R.id.tv_text)
    TextView tv_text;
    @Bind(R.id.listview)
    Button listview;
    public static HomeFragmentDemo newInstance() {
        HomeFragmentDemo fragment = new HomeFragmentDemo();
        return fragment;
    }
    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        Log.e( "initView: ","我创建了" );
    }
    @OnClick(R.id.eeee)
    void eeee() {
        jumpActivity(SqlLiteActivity.class);
    }
    @OnClick(R.id.listview)
    void listview() {
        jumpActivity(CeshiActivity.class);
    }

    @Override
    protected void initListeners() {
        netquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(1,"");
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if (tag == 1) {//?settingsid=1016&username=nrcrjb&password=nrcr123
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("settingsid","1058");
            parmMap.put("username","2001");
            parmMap.put("password","0");
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.SuccessWithData, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            Log.e( "OnSuccessResponse: ", resultInfo.getResponse().toString());
            jiexiXml(resultInfo.getResponse().toString());
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        Log.e( "OnFailedResponse: ", msg);
        //jiexiXml(jsonObject.optString("data"));
    }


    private void jiexiXml(String xml){
        Log.e( "jiexiXml: ",xml );
        //	String xml = "<VCOM version='1.1'><loginlink>11111023</loginlink><errmsg>dfft</errmsg></VCOM>";
        ByteArrayInputStream tInputStringStream = null;
        try
        {
            if (xml != null && !xml.trim().equals("")) {
                tInputStringStream = new ByteArrayInputStream(xml.getBytes());
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            tv_text.setText(e.getMessage());
            return;
        }
        XmlPullParser parser = Xml.newPullParser();
        try {
            try {
                parser.setInput(tInputStringStream, "UTF-8");
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                        // persons = new ArrayList<Person>();
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("content")) {
                            // currentPerson = new Person();
                            // currentPerson.setId(new
                            // Integer(parser.getAttributeValue(null, "id")));
                            try {
                                tv_text.setText(parser.nextText());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//      else if (currentPerson != null) {
//      if (name.equalsIgnoreCase("loginlink")) {
//       currentPerson.setName(parser.nextText());// 如果后面是Text节点,即返回它的值
//      } else if (name.equalsIgnoreCase("errmsg")) {
//       currentPerson.setAge(new Short(parser.nextText()));
//      }
//     }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
//     if (parser.getName().equalsIgnoreCase("person")
//       && currentPerson != null) {
//      persons.add(currentPerson);
//      currentPerson = null;
//     }
                        break;
                }
                eventType = parser.next();
            }
            tInputStringStream.close();
            // return persons;
            customLoadding.dismiss();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
