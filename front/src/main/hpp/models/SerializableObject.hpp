#ifndef GRAPHQL_DTO_SERIALIZABLE_HPP__
#define GRAPHQL_DTO_SERIALIZABLE_HPP__

#include <string>
#include <json/json.h>
#include "../IJsonSerializable.hpp"

namespace GraphQL::Dto
{

    class SerializableObject : public GraphQL::Json::IJsonSerializable
    {
        public:
            void Serialize( ::Json::Value& root );
            void Deserialize( ::Json::Value& root);
    
        private:
            int           m_nTestInt = 1;
            double        m_fTestFloat = 0.0;
            std::string   m_TestString = "toto";
            bool          m_bTestBool = true;
    };

}

#include "../../cpp/models/SerializableObject.cpp"

#endif  // GRAPHQL_DTO_SERIALIZABLE_HPP__

