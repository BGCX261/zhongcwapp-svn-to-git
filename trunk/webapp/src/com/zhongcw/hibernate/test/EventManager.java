package com.zhongcw.hibernate.test;

import org.hibernate.Query;
import org.hibernate.Session;

import com.zhongcw.hibernate.util.HibernateUtil;

import java.util.Date;
import java.util.List;

public class EventManager {

	public static void main(String[] args) {
		EventManager mgr = new EventManager();

		mgr.add();
		mgr.query();
	}

	private void add() {
		String title = "DDD";

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Event event = new Event();
		event.setTitle(title);

		session.save(event);
		session.getTransaction().commit();

		HibernateUtil.getSessionFactory().close();
	}

	private void query() {
		int recordbegin = 0;
		int pagesize = 100;

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Query query = session.createQuery("from Event");
		// start
		query.setFirstResult(recordbegin);
		// limit
		query.setMaxResults(pagesize);
		List list = query.list();

		session.getTransaction().commit();

		HibernateUtil.getSessionFactory().close();

		for (int i = 0; i < list.size(); i++) {
			Event event = (Event) list.get(i);
			System.out.println("=======  Title: " + event.getTitle());
		}

	}

}
