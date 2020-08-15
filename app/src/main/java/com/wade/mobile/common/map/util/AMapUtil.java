package com.wade.mobile.common.map.util;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.ai.cmcchina.crm.util.Global;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AMapUtil {
    public static final String HtmlBlack = "#000000";
    public static final String HtmlGray = "#808080";

    public static LatLonPoint getLatLonPoint(Marker marker) {
        LatLng latlng = marker.getPosition();
        return new LatLonPoint(latlng.latitude, latlng.longitude);
    }

    public static String checkEditText(EditText editText) {
        if (editText == null || editText.getText() == null || editText.getText().toString().trim().equals("")) {
            return "";
        }
        return editText.getText().toString().trim();
    }

    public static Spanned stringToSpan(String src) {
        if (src == null) {
            return null;
        }
        return Html.fromHtml(src.replace("\n", "<br />"));
    }

    public static String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("<font color=").append(color).append(">").append(src).append("</font>");
        return strBuf.toString();
    }

    public static String makeHtmlNewLine() {
        return "<br />";
    }

    public static String makeHtmlSpace(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append("&nbsp;");
        }
        return result.toString();
    }

    public static String getFriendlyLength(int lenMeter) {
        if (lenMeter > 10000) {
            return String.valueOf(lenMeter / 1000) + MapConstant.Kilometer;
        }
        if (lenMeter > 1000) {
            return String.valueOf(new DecimalFormat("##0.0").format((double) (((float) lenMeter) / 1000.0f))) + MapConstant.Kilometer;
        }
        if (lenMeter > 100) {
            return String.valueOf((lenMeter / 50) * 50) + MapConstant.Meter;
        }
        int dis = (lenMeter / 10) * 10;
        if (dis == 0) {
            dis = 10;
        }
        return String.valueOf(dis) + MapConstant.Meter;
    }

    public static boolean IsEmptyOrNullString(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<>();
        for (LatLonPoint point : shapes) {
            lineShapes.add(convertToLatLng(point));
        }
        return lineShapes;
    }

    public static String convertToTime(long time) {
        return new SimpleDateFormat(Global.DATE_FORMAT_1).format(new Date(time));
    }

    public static ImageView createMarkImageView(Context context, int drawableId) {
        ImageView imageView = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.width = 70;
        params.height = 70;
        imageView.setLayoutParams(params);
        imageView.setImageResource(drawableId);
        return imageView;
    }
}