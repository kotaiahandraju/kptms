package com.charvikent.issuetracking.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.charvikent.issuetracking.model.ReportIssue;
import com.charvikent.issuetracking.model.User;

@Repository
public class DashBoardDao {
	
	@PersistenceContext
	private EntityManager em;
	@Autowired
	HttpSession session;
	
	
	
	public Set<ReportIssue> getIssuesAssignBy(String id) {
		Set<ReportIssue> listissue=new TreeSet<ReportIssue>();

		try {
			@SuppressWarnings("unchecked")
			List <Object[]> rows=em.createQuery("select r.id , u.username, s.colour, p.priority,r.uploadfile,r.subject ,c.category,r.createdTime,r.status,ks.name,r.taskno from ReportIssue r, Category c, Priority p, User u, Severity s, KpStatus ks where r.assignto=u.id and r.kstatus=ks.id and p.id=r.priority and s.id=r.severity and c.id=r.category and r.kstatus<>'1'  and  r.assignby =:custName").setParameter("custName", id).getResultList();
			for(Object[] row: rows)
			{
				ReportIssue issue =new ReportIssue();
				int j = Integer.parseInt(String.valueOf(row[0]));
				Integer intobj=new Integer(j);
				issue.setId(intobj);
				issue.setAssignto((String) row[1]);
				issue.setSeverity((String) row[2]);
				issue.setPriority((String) row[3]);
				issue.setUploadfile((String) row[4]);
				issue.setSubject((String) row[5]);
				issue.setCategory((String) row[6]);
				issue.setCreatedTime((Date) row[7]);
				issue.setStatus( row[8].toString());         
				issue.setAssignby((String) row[9]);
				issue.setTaskno((String) row[10]);// assume setassignby is status of ticket status assigned to this variable
				listissue.add(issue);

			}
		} catch (Exception e) {
			System.out.println("error here");
			e.printStackTrace();
		}

		return  listissue;

	}
	
	 public static Integer assigntocount =null;
	public Set getIssuesAssignTo(String id) {
		Set<ReportIssue> listissue=new TreeSet<ReportIssue>();

		try {
			@SuppressWarnings("unchecked")
			List <Object[]> rows=em.createNativeQuery("select r.id , u.username, s.colour, p.priority,r.uploadfile,r.subject ,r.created_time,c.category,ks.name,ks.scolour,r.taskno from report_issue r, kpcategory c, kppriority p, kpusers u, kpseverity s ,kpstatus ks  where r.assignto=u.id and r.kstatus=ks.id and p.id=r.priority and s.id=r.severity and c.id=r.category  and r.kstatus in(2,6) and r.assignto =:custName").setParameter("custName", id).getResultList();
			for(Object[] row: rows)
			{
				ReportIssue issue =new ReportIssue();
				int j = Integer.parseInt(String.valueOf(row[0]));
				Integer intobj=new Integer(j);
				issue.setId(intobj);
				issue.setAssignby((String) row[1]);
				issue.setSeverity((String) row[2]);
				issue.setPriority((String) row[3]);
				issue.setUploadfile((String) row[4]);
				issue.setSubject((String) row[5]);
				issue.setCreatedTime((Date) row[6]);
				issue.setCategory((String) row[7]);
				issue.setAssignto((String) row[8]);                    // assume setassignby is status of ticket status assigned to this variable
				issue.setKstatus( (String) row[9]);                   // ticket colour assigned to this variable
				issue.setTaskno((String) row[10]);

				listissue.add(issue);

			}
		} catch (Exception e) {
			System.out.println("error here");
			e.printStackTrace();
		}
		
		assigntocount =listissue.size();

		return  listissue;

	}


	public Set getIssuesAssignToResolved(String id) {
		//List<ReportIssue> listissue=new ArrayList<>();
		
		Set<ReportIssue> listissue=new TreeSet<ReportIssue>();

		try {
			@SuppressWarnings("unchecked")
			List <Object[]> rows=em.createQuery("select r.id , u.username, s.colour, p.priority,r.uploadfile,r.subject ,r.createdTime,c.category,ks.scolour,ks.name ,r.taskno from ReportIssue r, Category c, Priority p, User u, Severity s, KpStatus ks   where  r.kstatus=ks.id and r.assignto=u.id and p.id=r.priority and s.id=r.severity and c.id=r.category  and r.kstatus='4' and  r.assignto =:custName").setParameter("custName", id).getResultList();
			for(Object[] row: rows)
			{
				ReportIssue issue =new ReportIssue();
				int j = Integer.parseInt(String.valueOf(row[0]));
				Integer intobj=new Integer(j);
				issue.setId(intobj);
				issue.setAssignby((String) row[1]);
				issue.setSeverity((String) row[2]);
				issue.setPriority((String) row[3]);
				issue.setUploadfile((String) row[4]);
				issue.setSubject((String) row[5]);
				issue.setCreatedTime((Date) row[6]);
				issue.setCategory((String) row[7]);
				issue.setKstatus((String) row[8]);
				issue.setAssignto((String) row[9]);
				issue.setTaskno((String) row[10]);


				listissue.add(issue);

			}
		} catch (Exception e) {
			System.out.println("error here");
			e.printStackTrace();
		}

		return  listissue;

	}
	
	
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer>  getGapAndCountForClosed() {

		List<ReportIssue> listissuegap=new ArrayList<>();
		ReportIssue issuegap =null;

		//String custName=null;

		List<Object[]> rows = 	em.createNativeQuery(" SELECT DATEDIFF(CURDATE(),created_time ) as gap ,count(id)  from report_issue where kstatus =:custName  group by gap  ").setParameter("custName", "1").getResultList();

		Map<Integer, Integer> issueTimelines = new HashMap<Integer, Integer>();

		for (Object[] row : rows) {
			issuegap = new ReportIssue();
			issuegap.setGapdays(Integer.parseInt(String.valueOf(row[0])));
			issuegap.setGapcount(Integer.parseInt(String.valueOf(row[1])));
			listissuegap.add(issuegap);

			issueTimelines.put(Integer.parseInt(String.valueOf(row[0])), Integer.parseInt(String.valueOf(row[1])));
		}
		return issueTimelines;

	}

	@SuppressWarnings("unchecked")
	public  Set<ReportIssue> getRecentlyModified(String id) {

		Set<ReportIssue> listissue=new TreeSet<ReportIssue>();

		try {
			List<Object[]> rows = em
			.createNativeQuery(" select   r.id , u.username, s.colour, p.priority,r.uploadfile,r.subject ,c.category,r.created_time,ks.name,ks.scolour,r.taskno from report_issue r, kpcategory c, kppriority p, kpusers u, kpseverity s,kpstatus ks  where r.kstatus=ks.id and r.assignto=u.id and p.id=r.priority and s.id=r.severity and c.id=r.category and r.kstatus='1'  and DATEDIFF (CURDATE(),r.updated_time )<=30 and  r.assignby =:custName union (select   r.id , u.username, s.colour, p.priority,r.uploadfile,r.subject ,c.category,r.created_time,ks.name,ks.scolour from report_issue r, category c, priority p, kpusers u, severity s,kpstatus ks  where r.kstatus=ks.id and r.assignto=u.id and p.id=r.priority and s.id=r.severity and c.id=r.category and r.kstatus='1'  and DATEDIFF (CURDATE(),r.updated_time )<=30 and  r.assignto =:custName )").setParameter("custName", id)
			.getResultList();
			for (Object[] row : rows) {
				ReportIssue issue = new ReportIssue();
				issue.setId(Integer.parseInt(String.valueOf(row[0])));
				issue.setAssignto((String) row[1]);
				issue.setSeverity((String) row[2]);
				issue.setPriority((String) row[3]);
				issue.setUploadfile((String) row[4]);
				issue.setSubject((String) row[5]);
				issue.setCategory((String) row[6]);
				issue.setCreatedTime((Date) row[7]);
				issue.setKstatus((String) row[9]);
				issue.setAssignby((String) row[8]);
				issue.setTaskno((String) row[9]);
				listissue.add(issue);

			}
		} catch (Exception e) {
			System.out.println("error here");
			e.printStackTrace();
		}

		return  listissue;


	}


	public Map<String,Integer> getSeverityCount() {
		
		User objuserBean = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String id=String.valueOf(objuserBean.getId());
		

		Map<String,Integer> statusCounts =new LinkedHashMap<String,Integer>();

		Integer opentotal=0;

		try {
			@SuppressWarnings("unchecked")
			List<Object[]> rows = em
			.createNativeQuery(" select ks.severity,count(*)as count from report_issue r,kpseverity ks" + 
					" where  r.severity=ks.id  and r.assignto =:id  and r.kstatus in(2,3) group by severity").setParameter("id", id).getResultList();
			for (Object[] row : rows) {
				
				opentotal=opentotal+Integer.parseInt(String.valueOf(row[1]));
				if(row[0].equals("Minor"))
				statusCounts.put((String)row[0], Integer.parseInt(String.valueOf(row[1])));
				else
				statusCounts.put("Minor", 0);	
				
				if(row[0].equals("Major"))
					statusCounts.put((String)row[0], Integer.parseInt(String.valueOf(row[1])));
					else
					statusCounts.put("Major", 0);	
				
				if(row[0].equals("Critical"))
					statusCounts.put((String)row[0], Integer.parseInt(String.valueOf(row[1])));
					else
					statusCounts.put("Critical", 0);
			}

			statusCounts.put("Open",opentotal);
		} catch (Exception e) {
			System.out.println("error here");
			e.printStackTrace();
		}
		
		
		for(Entry<String,Integer> entry :statusCounts.entrySet())
			System.out.println(entry.getKey()+" ......"+entry.getValue());
		return statusCounts;
	}

	

}
