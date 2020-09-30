package in.autodice.classmanagementsystem;

public class SingleMarkAttendance {
    String date,sname,status,bname,ADid,stname;
    public SingleMarkAttendance(String date,String subjectname,String status,String bname,String ADid,String stname){
        this.date=date;
        this.sname=subjectname;
        this.stname=stname;
        this.bname=bname;
        this.ADid=ADid;
        this.status=status;
    }
}
