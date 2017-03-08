/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
*/

/*

 .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.
| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
| |  ___  ____   | || |     _____    | || |  _________   | || |      __      | || |   ______     | || | _____  _____ | |
| | |_  ||_  _|  | || |    |_   _|   | || | |  _   _  |  | || |     /  \     | || |  |_   _ \    | || ||_   _||_   _|| |
| |   | |_/ /    | || |      | |     | || | |_/ | | \_|  | || |    / /\ \    | || |    | |_) |   | || |  | |    | |  | |
| |   |  __'.    | || |      | |     | || |     | |      | || |   / ____ \   | || |    |  __'.   | || |  | '    ' |  | |
| |  _| |  \ \_  | || |     _| |_    | || |    _| |_     | || | _/ /    \ \_ | || |   _| |__) |  | || |   \ `--' /   | |
| | |____||____| | || |    |_____|   | || |   |_____|    | || ||____|  |____|| || |  |_______/   | || |    `.__.'    | |
| |              | || |              | || |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'

 */

   
package com.example.jgraham.kitabureg1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

import java.util.ArrayList;

public class WidgetProvider extends AppWidgetProvider {
  public static String EXTRA_WORD=
    "com.example.jgraham.kitabureg1.WORD";
    RemoteViews widget;
    public static int BUTTON_PRESS=1;
    private static MySQLiteDbHelper mySQLiteDbHelper;
    public static ArrayList<KitabuEntry> kitabuEntries;
    //static int k=0;


  @Override
  public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
                       int[] appWidgetIds) {
    for (int i=0; i<appWidgetIds.length; i++) {
      Intent svcIntent=new Intent(ctxt, WidgetService.class);
      
      svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
      svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        mySQLiteDbHelper = MySQLiteDbHelper.getInstance(ctxt);



        //Log.d("aptest","cuttonpressvalcurrent "+BUTTON_PRESS);
        if(BUTTON_PRESS==1)
        {

            //Log.d("aptest","new buttonpressed1");
            svcIntent.putExtra("buttonpress",1);
        }
        else  if(BUTTON_PRESS==2)
        {

            //Log.d("aptest","new buttonpressed2");
            svcIntent.putExtra("buttonpress",2);
        }
        else if(BUTTON_PRESS==3)
        {
            svcIntent.putExtra("buttonpress",3);
        }


      widget=new RemoteViews(ctxt.getPackageName(),
                                          R.layout.widget);
      widget.setRemoteAdapter(appWidgetIds[i], R.id.words,
                              svcIntent);

      Intent clickIntent=new Intent(ctxt, LoremActivity.class);
      PendingIntent clickPI= PendingIntent
                              .getActivity(ctxt, 0,
                                            clickIntent,
                                            0);
        /*widget.setOnClickPendingIntent(R.id.actionButton, clickPI);*/



       // Register an onClickListener for 1st button
        Intent intent1 = new Intent(ctxt, WidgetProvider.class);

        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
        intent1.putExtra("key", "public");

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(ctxt,
                1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        widget.setOnClickPendingIntent(R.id.btn_public, pendingIntent1);
        // Register an onClickListener for 1st button
        Intent intent2 = new Intent(ctxt, WidgetProvider.class);

        intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
        intent2.putExtra("key", "private");

        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(ctxt,
                2, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        widget.setOnClickPendingIntent(R.id.btn_private
                , pendingIntent2);
        // Register an onClickListener for 1st button
        Intent intent3 = new Intent(ctxt, WidgetProvider.class);

        intent3.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
        intent3.putExtra("key", "suggestion");

        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(ctxt,
                3, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

        widget.setOnClickPendingIntent(R.id.btn_suggestion, pendingIntent3);


        widget.setPendingIntentTemplate(R.id.words, clickPI);

      appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
    }
    
    super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
  }

    String value;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle b=intent.getExtras();


        try{
            value=b.getString("key");
            if(value.equals("public"))
            {
               /* widget.setInt(R.id.btn_public, "setBackgroundColor",Color.parseColor("#DE5A00"));
                widget.setInt(R.id.btn_private, "setBackgroundColor", Color.parseColor("#C7D4DD"));
                widget.setInt(R.id.btn_suggestion, "setBackgroundColor", Color.parseColor("#C7D4DD"));
*/
                BUTTON_PRESS=1;
                //Log.d("aptest","buttonpressed");
                //Log.d("aptest","buttonpressed->"+BUTTON_PRESS);
                kitabuEntries=mySQLiteDbHelper.fetchPublicEntries();
                int size=0;
                if(kitabuEntries.size()<10)
                {
                    size=kitabuEntries.size();
                    for(int i=0;i<size;i++) {
                        LoremViewsFactory.items[i] = kitabuEntries.get(i).getmLink().toString();
                    }
                    for(int i=size;i<10;i++)
                    {
                        LoremViewsFactory.items[i]="";
                    }
                }
                else
                {
                    size=10;
                    for(int i=0;i<size;i++) {
                        LoremViewsFactory.items[i] = kitabuEntries.get(i).getmLink().toString();
                    }
                }                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, WidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.words);
            }
            if(value.equals("private"))
            {
              /*  widget.setInt(R.id.btn_public, "setBackgroundColor", Color.parseColor("#C7D4DD"));
                widget.setInt(R.id.btn_private, "setBackgroundColor", Color.parseColor("#DE5A00"));
                widget.setInt(R.id.btn_suggestion, "setBackgroundColor", Color.parseColor("#C7D4DD"));
         */       BUTTON_PRESS=2;
                //Log.d("aptest","buttonpressed");
                //Log.d("aptest","buttonpressed->"+BUTTON_PRESS);
                kitabuEntries=mySQLiteDbHelper.fetchPrivateEntries();
                int size=0;
                if(kitabuEntries.size()<10)
                {
                    size=kitabuEntries.size();
                    for(int i=0;i<size;i++) {
                        LoremViewsFactory.items[i] = kitabuEntries.get(i).getmTitle().toString();
                    }
                    for(int i=size;i<10;i++)
                    {
                        LoremViewsFactory.items[i]="";
                    }
                }
                else
                {
                    size=10;
                    for(int i=0;i<size;i++) {
                        LoremViewsFactory.items[i] = kitabuEntries.get(i).getmTitle().toString();
                    }
                }

                //LoremViewsFactory.items2[0]="Aditi";
                //LoremViewsFactory.items=LoremViewsFactory.items2;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, WidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.words);
            }
            if(value.equals("suggestion"))
            {
          /*      widget.setInt(R.id.btn_public, "setBackgroundColor", Color.parseColor("#C7D4DD"));
                widget.setInt(R.id.btn_private, "setBackgroundColor", Color.parseColor("#C7D4DD"));
                widget.setInt(R.id.btn_suggestion, "setBackgroundColor", Color.parseColor("#DE5A00"));
     */           BUTTON_PRESS=3;
                //Log.d("aptest","buttonpressed");
                //Log.d("aptest","buttonpressed->"+BUTTON_PRESS);
                kitabuEntries=mySQLiteDbHelper.fetchNotificationEntries();
                int size=0;
                if(kitabuEntries.size()<10)
                {
                    size=kitabuEntries.size();
                    for(int i=0;i<size;i++) {
                        LoremViewsFactory.items[i] = kitabuEntries.get(i).getmTitle().toString();
                    }
                    for(int i=size;i<10;i++)
                    {
                        LoremViewsFactory.items[i]="";
                    }
                }
                else
                {
                    size=10;
                    for(int i=0;i<size;i++) {
                        LoremViewsFactory.items[i] = kitabuEntries.get(i).getmLink().toString();
                    }
                }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, WidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.words);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        super.onReceive(context, intent);

    }

}