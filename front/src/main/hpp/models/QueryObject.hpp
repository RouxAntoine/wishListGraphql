#ifndef GRAPHQL_DTO_QUERY_HPP__
#define GRAPHQL_DTO_QUERY_HPP__

#include <string>
#include <json/json.h>
#include "../IJsonSerializable.hpp"

namespace GraphQL::Dto
{

    class QueryObject : public GraphQL::Json::IJsonSerializable
    {
        public:
            QueryObject(std::string query, std::string op): query(query), operationName(op) {};      // GraphQL::Json::IJsonSerializable();
            void Serialize( ::Json::Value& root );
            void Deserialize( ::Json::Value& root);
    
        private:
            std::string operationName = "";
            std::string query = "";
            std::string variables;
    };

}

#include "../../cpp/models/QueryObject.cpp"

#endif  // GRAPHQL_DTO_QUERY_HPP__
