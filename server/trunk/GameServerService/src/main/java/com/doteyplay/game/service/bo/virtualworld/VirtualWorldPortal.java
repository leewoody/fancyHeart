package com.doteyplay.game.service.bo.virtualworld;

import com.doteyplay.core.bhns.AbstractSimpleService;
import com.doteyplay.core.bhns.portal.AbstractLocalPortal;
import com.doteyplay.core.bhns.source.IEndpointSource;
import com.doteyplay.core.bhns.source.IServiceOption;
import com.doteyplay.game.service.pipeline.DefaultServicePipeline;

public class VirtualWorldPortal extends AbstractLocalPortal<IVirtualWorldService>
{
	VirtualWorldService _portalService;
	
	public VirtualWorldPortal(IServiceOption option, IEndpointSource source)
	{
		super(option, source);
		_portalService=new VirtualWorldService();
	}

	@Override
	public void initEventListerner()
	{

	}

	@Override
	public void initializePortal()
	{
		this.bindPortalService(_portalService);
		this.bindPipline(DefaultServicePipeline.getInstance());
	}

	@Override
	protected void initializeService(AbstractSimpleService<IVirtualWorldService> arg0)
	{
		((VirtualWorldService)arg0).initialize();
	}

	@Override
	public void releaseService(IVirtualWorldService arg0)
	{

	}

}
