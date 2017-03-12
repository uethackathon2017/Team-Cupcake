package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.FacultyObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.HospitalObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.RequestObject;

/**
 * Created by NgocThai on 11/03/2017.
 */

public class SQLController {

    private SQLHelper sqlHelper;

    public SQLController(Context context) {
        sqlHelper = new SQLHelper(context);
    }

    public boolean insertHospital(HospitalObject hospitalObject) {
        boolean result = false;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        try {
            ContentValues vl = new ContentValues();
            vl.put(SQLHelper.HOS_ID, hospitalObject.getId());
            vl.put(SQLHelper.HOS_NAME, hospitalObject.getName());
            vl.put(SQLHelper.HOS_KIND, hospitalObject.getKind());
            vl.put(SQLHelper.HOS_LIMIT, hospitalObject.getLimit());
            vl.put(SQLHelper.HOS_ADDRESS, hospitalObject.getAddress());
            vl.put(SQLHelper.HOS_LATITUDE, hospitalObject.getLatitude());
            vl.put(SQLHelper.HOS_LONGITUDE, hospitalObject.getLongitude());
            vl.put(SQLHelper.HOS_PHONE, hospitalObject.getPhone());
            vl.put(SQLHelper.HOS_IMAGE, hospitalObject.getImage());
            vl.put(SQLHelper.HOS_DESCRIPTION, hospitalObject.getDesc());
            vl.put(SQLHelper.HOS_RATE, hospitalObject.getRate());
            long kq = db.insert(SQLHelper.TABLE_NAME_HOSPITAL, null, vl);
            if (kq > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    public boolean insertHistoryReq(String symptom, String reqTime, String dayTarget, String nameFaculty) {
        boolean result = false;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        try {
            ContentValues vl = new ContentValues();
            vl.put(SQLHelper.HIS_SYMPTOM, symptom);
            vl.put(SQLHelper.HIS_REQ_TIME, reqTime);
            vl.put(SQLHelper.HIS_DAY_TARGET, dayTarget);
            vl.put(SQLHelper.HIS_NAME_FACULTY, nameFaculty);
            long kq = db.insert(SQLHelper.TABLE_NAME_HISTORY, null, vl);
            if (kq > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    public boolean insertHistoryIdReq(int id) {
        boolean result = false;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        try {
            ContentValues vl = new ContentValues();
            vl.put(SQLHelper.HIS_ID_REQUEST, id);
            long kq = db.insert(SQLHelper.TABLE_NAME_HISTORY, null, vl);
            if (kq > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    public boolean insertHistoryRes(String timeStart, String timeEnd, String doctorDesc, String doctorName, String responseDesc, String roomName, int idRequest) {
        boolean result = false;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        try {
            ContentValues vl = new ContentValues();
            vl.put(SQLHelper.HIS_TIME_START, timeStart);
            vl.put(SQLHelper.HIS_TIME_END, timeEnd);
            vl.put(SQLHelper.HIS_DOCTOR_DESC, doctorDesc);
            vl.put(SQLHelper.HIS_DOCTOR_NAME, doctorName);
            vl.put(SQLHelper.HIS_RESPONSE_DESC, responseDesc);
            vl.put(SQLHelper.HIS_RESPONSE_DESC, responseDesc);
            vl.put(SQLHelper.HIS_ROOM_NAME, roomName);
            long kq = db.update(SQLHelper.TABLE_NAME_HISTORY, vl, SQLHelper.HIS_ID_REQUEST + " = ", null);
            if (kq > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }


    public boolean insertFaculty(FacultyObject facultyObject) {
        boolean result = false;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues vl = new ContentValues();
        try {
            vl.put(SQLHelper.FAC_ID, facultyObject.getId());
            vl.put(SQLHelper.FAC_NAME, facultyObject.getName());
            vl.put(SQLHelper.FAC_DESCRIPTION, facultyObject.getDescription());
            vl.put(SQLHelper.FAC_ID_HOSPITAL, facultyObject.getIdHospital());
            vl.put(SQLHelper.FAC_KIND, facultyObject.getKind());
            long kq = db.insert(SQLHelper.TABLE_NAME_FACULTY, null, vl);
            if (kq > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    public boolean insertRequest(RequestObject requestObject) {
        boolean result = false;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues vl = new ContentValues();
        try {
            vl.put(SQLHelper.REQ_ID, requestObject.getId());
            vl.put(SQLHelper.REQ_ID_FACULTY, requestObject.getIdFaculty());
            vl.put(SQLHelper.REQ_CHECKED, requestObject.getChecked());
            vl.put(SQLHelper.REQ_DAY_TARGET, requestObject.getDayTarget());
            vl.put(SQLHelper.REQ_ID_PATIENT, requestObject.getIdPatient());
            vl.put(SQLHelper.REQ_SYMPTOM, requestObject.getSymptom());
            vl.put(SQLHelper.REQ_TIME, requestObject.getRequestTime());
            long kq = db.insert(SQLHelper.TABLE_NAME_REQUEST, null, vl);
            if (kq > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }


    public ArrayList<HospitalObject> queryListHospital(String sql) {
        ArrayList<HospitalObject> ls = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        try {
            Cursor cs = db.rawQuery(sql, null);
            while (cs.moveToNext()) {
                ls.add(new HospitalObject(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getInt(3), cs.getString(4),
                        cs.getString(5), cs.getString(6), cs.getString(7), cs.getString(8), cs.getString(9), cs.getDouble(10)));
            }
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ls;
    }
    public ArrayList<HospitalObject> queryListHistory(String sql) {
        ArrayList<HospitalObject> ls = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        try {
            Cursor cs = db.rawQuery(sql, null);
            while (cs.moveToNext()) {
                ls.add(new HospitalObject(cs.getInt(0), cs.getString(1), cs.getString(2), cs.getInt(3), cs.getString(4),
                        cs.getString(5), cs.getString(6), cs.getString(7), cs.getString(8), cs.getString(9), cs.getDouble(10)));
            }
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ls;
    }

    public ArrayList<RequestObject> queryListRequest(String sql) {
        ArrayList<RequestObject> ls = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        try {
            Cursor cs = db.rawQuery(sql, null);
            while (cs.moveToNext()) {
                ls.add(new RequestObject(cs.getInt(0), cs.getString(1), cs.getColumnName(2), cs.getInt(3), cs.getInt(4), cs.getString(5)));
            }
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ls;
    }

    public ArrayList<FacultyObject> queryFaculty(String sql) {
        ArrayList<FacultyObject> ls = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                ls.add(new FacultyObject(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ls;
    }


    public boolean deleteAllData(String table) {
        boolean result = false;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        try {
            long kq = db.delete(table, null, null);
            if (kq > -1) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

}
