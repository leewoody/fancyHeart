package com.doteyplay.core.bhns;

import org.apache.log4j.Logger;

import com.doteyplay.core.bhns.location.ISvrLocationService;
import com.doteyplay.core.bhns.source.IEndpointSource;
import com.doteyplay.core.bhns.source.IServiceOption;
import com.doteyplay.core.bhns.source.IServiceSource;
import com.doteyplay.core.bhns.source.options.AppertainServiceOption;
import com.doteyplay.core.bhns.source.options.ClusterServiceOption;
import com.doteyplay.core.bhns.source.options.NormalServiceOption;
import com.doteyplay.core.bhns.source.source.AppertainSource;
import com.doteyplay.core.bhns.source.source.ClusterSource;
import com.doteyplay.core.bhns.source.source.NormalSource;

/**
 * �������������Ϣ
 * 
 * @author 
 * 
 */
public class ServiceInfo
{
	protected static final Logger logger = Logger.getLogger(ServiceInfo.class);

	private IServiceOption serviceOption;// ����������������Ϣ
	private IServiceSource serviceSource;// �����Դ��

	public ServiceInfo(IServiceOption serviceOption)
	{
		this.serviceOption = serviceOption;
		if (this.serviceOption != null)
		{
			switch (option().configType())
			{
			case IServiceOption.CONFIG_TYPE_NORMAL:
				serviceSource = new NormalSource((NormalServiceOption) serviceOption);
				break;
			case IServiceOption.CONFIG_TYPE_CLUSTER:
				serviceSource = new ClusterSource((ClusterServiceOption) serviceOption);
				break;
			case IServiceOption.CONFIG_TYPE_APPERTAIN:
				serviceSource = new AppertainSource((AppertainServiceOption) serviceOption);
				break;
			default:
				logger.error("invalid option type");
				break;
			}
		}
	}

	public int getPortalId()
	{
		return serviceOption.portalId();
	}

	public boolean isAvailable()
	{
		return serviceSource.isAvailable();
	}

	public IServiceOption option()
	{
		return serviceOption;
	}

	public IServiceSource source()
	{
		return serviceSource;
	}

	public void initialSource()
	{
		if (serviceSource != null)
		{
			this.serviceSource.initialize();
		}
		else
		{
			logger.error("initialSource error:portalId=" + this.getPortalId());
		}
	}

	public boolean isProxy(byte endpointId)
	{
		return !serviceSource.isLocalImplemention(endpointId);
	}
	
	public String postPortalCommand(int portalId,String requestCommand,byte endpointId)
	{
		return serviceSource.postPortalCommand(portalId,requestCommand,endpointId);
	}
	/**
	 * 
	 * @param svrid ��ɫId
	 * @param endpointId ���Id
	 * @param isOrderActive true
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ISimpleService> T activeService(long svrid, byte endpointId, boolean isOrderActive)
	{
		return (T)serviceSource.activeService(svrid, endpointId, isOrderActive);
	}

	public void destroyService(long svrid)
	{
		serviceSource.destroyService(svrid);
	}
	
	public void destoryAllService(long svrid)
	{
		if(serviceSource instanceof ClusterSource)
		{
			ISvrLocationService locationService = BOServiceManager.findLocalDefaultService(ISvrLocationService.PORTAL_ID);
			byte endpointId = locationService.getEndpointIdOfserviceId(this.getPortalId(), svrid);
			if(endpointId >= 0)
			{
				IEndpointSource source =  serviceSource.getEndpoint(endpointId);
				if(source != null)
					source.destroyService(svrid);
			}
		}else
			destroyService(svrid);
	}

	@SuppressWarnings("unchecked")
	public <T extends ISimpleService> T findService(int portalId, long svrid)
	{
		return (T)serviceSource.findService(portalId, svrid);
	}

	@SuppressWarnings("unchecked")
	public <T extends ISimpleService> T getPortalService(int portalId, byte endpointId)
	{
		return (T)serviceSource.getPortalService(portalId, endpointId);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ISimpleService> T getLocalPortalService(int portalId)
	{
		return (T)serviceSource.getLocalPortalService(portalId);
	}

	public void triggerEvent(int eventid, String methodSignature, Object[] args)
	{
		serviceSource.triggerEvent(eventid, methodSignature, args);
	}

	public void syncServiceLocation(long svrid, byte endpointId)
	{

	}
}