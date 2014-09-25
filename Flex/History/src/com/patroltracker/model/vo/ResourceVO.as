package com.patroltracker.model.vo
{
	import org.puremvc.as3.patterns.proxy.Proxy;
	
	public class ResourceVO
	{
		public var proxyName:String;
		public var loaded:Boolean;
		public var blockChain:Boolean;
		
		function ResourceVO( proxyName:String, blockChain:Boolean )
		{
			this.proxyName = proxyName;
			this.loaded = false;
			this.blockChain = blockChain;
		}
	}
	
}
