var app = angular.module("myApp");

app.factory('MessageService',['$http', function ($http) {
	var messageService = {};
	var urlBase = 'rest/message/';
	
	messageService.insert = function(message) {
		return $http.post(urlBase + "insert", message);
	}
	
	messageService.get = function(messageId) {
		return $http.get(urlBase+ "get/" + messageId);
	}
	messageService.getAll = function(amount) {
		return $http.get(urlBase+ "all/"+amount);
	}
	messageService.calculate = function(text) {
		return $http.get(urlBase+ "calculate/"+text);
	}
	
	
	return messageService;
}]);