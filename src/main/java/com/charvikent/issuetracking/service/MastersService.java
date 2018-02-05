package com.charvikent.issuetracking.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.charvikent.issuetracking.dao.MastersDao;
import com.charvikent.issuetracking.model.Department;
import com.charvikent.issuetracking.model.KpStatus;
import com.charvikent.issuetracking.model.User;

@Service
@Transactional
public class MastersService {
	
	@Autowired
	MastersDao mastersDao;
	
	@Autowired
	HttpSession session;
	
	
	
	
	
	public List<Department> deptList()
	{
		 List<Department> deptList= mastersDao.getDepartmentNames();
		return deptList;
		
	}
	
	
	
	public Map<Integer, String> getDepartmentNames()
	{
		Map<Integer, String> rolesMap = new LinkedHashMap<Integer, String>();
		try
		{
		List<Department> rolesList= mastersDao.getDepartmentNames();
		for(Department bean: rolesList){
			rolesMap.put(bean.getId(), bean.getName());
		}
				
	} catch (Exception e) {
		e.printStackTrace();
	}
		return rolesMap;
				
		
	}
	
	
	
	
	public Map<Integer, String> getSortedDepartments()
	{
		Map<Integer, String> rolesMap = new LinkedHashMap<Integer, String>();
		User objuserBean = (User) session.getAttribute("cacheUserBean");
		try
		{
		List<Department> rolesList= mastersDao.getDepartmentNames();
		if(objuserBean.getDesignation().equals("1"))
		{
		for(Department bean: rolesList){
			rolesMap.put(bean.getId(), bean.getName());
		}
			
		}
		else
			
		{
			System.out.println(objuserBean.getDepartment());
			
			
			
			for(Department bean: rolesList){
				System.out.println(bean.getId());
				if(objuserBean.getDepartment().equals(String.valueOf(bean.getId())))
				rolesMap.put(bean.getId(), bean.getName());
			}
			
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
		return rolesMap;
				
		
	}
	
	
	
	
	
	
	
	public void saveDept(Department dept)
	{
		mastersDao.saveDept(dept);
	}
	
	
	

	public Department getDepartmentsById(Department dept) {
		return mastersDao.getDepartmentById(dept);
	}

	public void updateDept(Department dept) {
		mastersDao.updateDept(dept);
		
	}

	public boolean deleteDepartment(Integer id, String status) {
		return mastersDao.deleteDepartment(id,status);
	}
	
	public HashMap<Integer, String> getKpStatues()
	{
		List<KpStatus> listkpstatues=mastersDao.getKpStatues();
		
		
		HashMap<Integer,String> kpstatuesmap =new HashMap<Integer,String>();
		
		for(KpStatus kp:listkpstatues)
		{
			kpstatuesmap.put(kp.getId(), kp.getName());
			
		}
		
		return kpstatuesmap;
	}
	
	
	

	
	

}
