#ifndef GRAPHQL_DTO_WISH_HPP__
#define GRAPHQL_DTO_WISH_HPP__

#include <string>
#include <json/json.h>
#include "../IJsonSerializable.hpp"

namespace GraphQL::Dto
{

    class WishObject : public GraphQL::Json::IJsonSerializable
    {
        public:
            WishObject(std::string id): id(id) {};      // GraphQL::Json::IJsonSerializable();
            void Serialize( ::Json::Value& root );
            void Deserialize( ::Json::Value& root);
    
        private:
            std::string id;
            std::string name;
            std::string description;
            double price = "";
    };

}

#include "../../cpp/models/WishObject.cpp"

#endif  // GRAPHQL_DTO_WISH_HPP__