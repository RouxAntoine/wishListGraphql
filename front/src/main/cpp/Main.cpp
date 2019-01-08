#include "../hpp/GraphQLHTTP.hpp"
#include "../hpp/models/SerializableObject.hpp"
#include "../hpp/models/QueryObject.hpp"
#include <sstream>
#include <string>
#include <iostream>
#include <memory>               // for use of std::unique_ptr

#define DEBUG 0	// 1

// test post verb with class body
void testPostClass()
{
    std::string graphqlQuery = "query wishes {wishes {id name description price owner { ...user}}} fragment user on User {id firstName lastName mail}";
    std::string op = "wishes";

	const std::string url = "http://dx30:8082/api/endpoint";
	// const std::string url = "http://localhost:8080/api/endpoint";

    GraphQL::Http::HttpClient http(DEBUG);
    GraphQL::Dto::QueryObject query(graphqlQuery, op);

    std::string data = http.post<GraphQL::Dto::QueryObject>(url, query, {
		{"content-type","application/json;charset=UTF-8"}
    });

    std::cout << "post : " << data << std::endl << std::endl;

    // GraphQL::Dto::SerializableObject* object2 = new GraphQL::Dto::SerializableObject();
    // bool success = GraphQL::Json::CJsonSerializer::Deserialize(object2, data);
    // if(success) {
    //     std::string res;
    //     success = GraphQL::Json::CJsonSerializer::Serialize(object2, res);
    //     if(success) {
    //         std::cout << res << std::endl << std::endl;
    //     }
    // }
}

// test get verb
void testGet()
{
	const std::string url = "https://api-adresse.data.gouv.fr/search/";
    GraphQL::Http::HttpClient http(DEBUG);

    std::string data = http.get(url, {{"q", "8 bd du port"}},
    {
		{"Content-Type","application/text"},
        {"authentification","key"}
    });

    std::cout << data << std::endl << std::endl;
}

int main(int argc, char** const argv)
{

    // testGet();
    testPostClass();

    return EXIT_SUCCESS;
}

//  std::string searchedAddress;	// for example : "8 bd du port";
//  std::stringstream ss;
//	if(argc < 2) {
//		fprintf(stderr, "Bad parameter give location to search\n");
//		exit(EXIT_FAILURE);
//	}
//
//	for(size_t i = 1; i < argc; ++i) {
//		if(i != 1)
//			ss << " ";
//		ss << argv[i];
//	}
//	searchedAddress = ss.str();

//	// init jsoncpp
//	Json::Value root;
//	std::string errs;
//	Json::CharReaderBuilder rbuilder;
//  cfg >> rbuilder.settings_;
//  std::unique_ptr<Json::CharReader> const reader(rbuilder.newCharReader());
//
//  bool res = reader->parse(buffer.c_str(), buffer.c_str() + buffer.size(), &root, &errs);
//	if(!res) {
//		fprintf(stderr, "Failed Error during Json parse %s \n", errs.c_str());
//		exit(EXIT_FAILURE);
//	}

//	for(auto suggested : root["features"]) {
//		std::cout << suggested["properties"]["label"] << std::endl;
//	}