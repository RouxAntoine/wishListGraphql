#ifndef GRAPHQL_JSON_HPP__
#define GRAPHQL_JSON_HPP__

#include <json/json.h>

namespace GraphQL::Json
{
    class IJsonSerializable {
        public:
            virtual void Serialize( ::Json::Value& root ) = 0;
            virtual void Deserialize( ::Json::Value& root) = 0;
    };

    class CJsonSerializer
    {
        public:
            static bool Serialize(IJsonSerializable* pObj, std::string& output );
            static bool Deserialize(IJsonSerializable* pObj, std::string& input );
    
        private:
            CJsonSerializer( void ) {};
    };
}

#include "../cpp/IJsonSerializable.cpp"

#endif  // GRAPHQL_JSON_HPP__
