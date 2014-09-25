package com.patroltracker.model.business
{
	import flash.utils.ByteArray;
	
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;

	public class LoadHistoryDelegate
	{
		private var responder : IResponder;
		private var service : HTTPService;
		
		public function LoadHistoryDelegate( responder : IResponder, url:String) 
		{
			service = new HTTPService();
			service.resultFormat = 'object';
			service.method = 'POST';
			service.url = url;
		/*	service.requestTimeout = 60;
			service.contentType = "application/json";*/
		
			
			
			
			this.responder = responder;
		}

		public function load() : void 
		{
	/*		var data:Object = JSON.stringify({"longitude" : -65.818333,"latitude" : -31.492499});
			
			var postData:ByteArray = new ByteArray();
			postData.writeUTFBytes(data ? data.toString() : "");
			postData.position = 0;*/
			
			var token:AsyncToken = service.send(/*postData*/);
			token.addResponder( responder );
		}
	}
}