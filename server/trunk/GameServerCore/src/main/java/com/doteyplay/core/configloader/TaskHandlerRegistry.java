package com.doteyplay.core.configloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.doteyplay.core.server.servertaskimpl.TimerTaskInfo;
import com.doteyplay.core.util.CronExpression;
import com.doteyplay.core.util.PackageScaner;
import com.doteyplay.core.util.StrUtils;
import com.doteyplay.core.util.servertask.IServerTaskDBManager;
import com.doteyplay.core.util.servertask.ServerTask;
import com.doteyplay.core.util.servertask.ServerTaskInfo;
import com.doteyplay.core.util.servertask.ServerTaskType;

/**
 * 计划任务服务注册
 * 
 * @author
 * 
 */
public class TaskHandlerRegistry
{

	private static final String CONFIG_FILE = "task-handler-config.xml";

	private List<TimerTaskInfo> taskInfo = new ArrayList<TimerTaskInfo>();

	private Class<ServerTask<ServerTaskInfo, IServerTaskDBManager>> taskHandlerClz;

	private IServerTaskDBManager dbManager;

	private TaskHandlerRegistry()
	{
		loadConfig();
	}

	private void loadConfig()
	{ // 读取配置文件参数
		taskInfo.clear();
		SAXReader saxReader = new SAXReader();
		try
		{
			Document doc = saxReader.read(MessageRegistry.class
					.getClassLoader().getResourceAsStream(CONFIG_FILE));
			@SuppressWarnings("unchecked")
			List<Node> list = doc.selectNodes("//task");
			Element handlerE = (Element) doc.selectSingleNode("//handler");
			// 设定主类和参数类
			this.taskHandlerClz = (Class<ServerTask<ServerTaskInfo, IServerTaskDBManager>>) Class
					.forName(handlerE.attributeValue("class"));
			Class<?> dbClz = Class.forName(handlerE
					.attributeValue("dbManagerClass"));
			this.dbManager = (IServerTaskDBManager) dbClz.newInstance();
			if (list != null)
			{
				for (Node taskNode : list)
				{
					TimerTaskInfo info = new TimerTaskInfo();
					// 设定名称
					Element elm = (Element) taskNode;
					info.setPKId(StrUtils.parseInt(elm.attributeValue("uid"), 0));
					info.setName(elm.attributeValue("id"));
					processTask(info, taskNode);
					taskInfo.add(info);
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void processTask(TimerTaskInfo info, Node task) throws Exception
	{
		// 设置时间
		Element elm = (Element) task.selectSingleNode("time");
		if (elm != null)
		{
			String time = elm.attributeValue("cron-expression");
			parseTime(info, time);
			info.setSchedulePrecision(StrUtils.parseInt(
					elm.attributeValue("precision"), 0));
		}

		// 设置加载项
		Node node = task.selectSingleNode("items");
		if (node != null)
		{
			info.setItemClassName(parseTaskItem(node));
		}
	}

	private void parseTime(TimerTaskInfo info, String time) throws Exception
	{
		CronExpression e = new CronExpression(time);
		int minute = e.getMinute();
		int hour = e.getHour();
		int dayOfMonth = e.getDayOfMonth();
		int dayOfWeek = e.getDayOfWeek();
		int month = e.getMonth();

		// 暂时不支持每小时。这个获取解析的方式要变更，需要的时候再写吧.应该是*/*的写法解析反馈.
		ServerTaskType type;
		if (month != -1)
		{
			type = ServerTaskType.YEARLY;
		} else if (dayOfMonth != -1)
		{
			type = ServerTaskType.MONTHLY;
		} else if (dayOfWeek != -1)
		{
			type = ServerTaskType.WEEKLY;
		} else
		{
			type = ServerTaskType.DAILY;
		}

		info.setMinute(minute);
		info.setHour(hour);
		info.setDate(dayOfMonth);
		info.setMonth(month);
		info.setWeek(dayOfWeek);
		info.setTaskType(type.ordinal());
	}

	private String[] parseTaskItem(Node items)
	{

		Map<String, ItemBean> taskItemMap = new HashMap<String, ItemBean>();
		String ext = ".class";
		// 先检查Package
		@SuppressWarnings("unchecked")
		List<Node> packages = items.selectNodes("package");
		if (packages != null)
		{
			for (Node p : packages)
			{

				String value = ((Element) p).attributeValue("value");
				// 解析不包括的
				@SuppressWarnings("unchecked")
				List<Node> exclude = p.selectNodes("exclude");
				Map<String, String> excludeClass = new HashMap<String, String>();
				if (exclude != null)
				{
					String ex;
					for (Node e : exclude)
					{
						ex = ((Element) e).attributeValue("class");
						excludeClass.put(ex, ex);
					}
				}

				// 分析package下的文件[不支持子]

				String[] include = PackageScaner.scanNamespaceFiles(value, ext,
						false,false);
				if (include != null && include.length > 0)
				{
					ItemBean bean;
					String key;
					for (String includeClass : include)
					{
						key = value
								+ "."
								+ includeClass.subSequence(0,
										includeClass.length() - (ext.length()));
						if (!excludeClass.containsKey(key))
						{
							bean = new ItemBean(key, 0); // 0认为是默认，不会变更
							taskItemMap.put(key, bean);
						}
					}
				}
			}
		}

		// 检查额外包括的内容
		@SuppressWarnings("unchecked")
		List<Node> include = items.selectNodes("include");
		if (include != null)
		{
			String key;
			Element e;
			ItemBean bean;
			for (Node node : include)
			{
				e = (Element) node;
				key = e.attributeValue("class");
				int order = StrUtils.parseInt(e.attributeValue("order"), 0);
				if (taskItemMap.containsKey(key))
				{
					bean = taskItemMap.get(key);
					bean.setOrder(order);
				} else
				{
					bean = new ItemBean(key, order);
					taskItemMap.put(key, bean);
				}
			}
		}

		// 排序
		ItemBean[] array = new ItemBean[taskItemMap.size()];
		taskItemMap.values().toArray(array);
		Arrays.sort(array);

		String[] result = new String[array.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = array[i].getClassName();
		}

		return result;
	}

	public List<TimerTaskInfo> getTaskInfo()
	{
		return taskInfo;
	}

	public Class<ServerTask<ServerTaskInfo, IServerTaskDBManager>> getTaskHandlerClz()
	{
		return taskHandlerClz;
	}

	public IServerTaskDBManager getDbManager()
	{
		return dbManager;
	}

	private static TaskHandlerRegistry _instance = new TaskHandlerRegistry();

	public static TaskHandlerRegistry getInstance()
	{
		return _instance;
	}

	private static class ItemBean implements Comparable<ItemBean>
	{

		private String className;
		private int order;

		public ItemBean(String className, int order)
		{
			this.className = className;
			this.order = order;
		}

		public String getClassName()
		{
			return className;
		}

		public int getOrder()
		{
			return order;
		}

		public void setOrder(int order)
		{
			this.order = order;
		}

		@Override
		public int compareTo(ItemBean o)
		{
			return order - o.getOrder();
		}

	}

	// ////////////////////////////////////////////////////////////////////

	public static void reload()
	{
		getInstance().loadConfig();
	}

	public static void main(String[] args)
	{
		TaskHandlerRegistry.getInstance();
	}

}
