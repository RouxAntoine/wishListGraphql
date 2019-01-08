#ifdef GRAPHQL_JSON_HPP__

#include <json/json.h>
#include <json/writer.h>
#include <memory>
#include <sstream>
#include <iostream>

namespace GraphQL::Json
{

    bool CJsonSerializer::Serialize(IJsonSerializable* pObj, std::string& output )
    {
        if (pObj == NULL)
            return false;
        
        // cast object to ::Json::value
        ::Json::Value serializeRoot;
        pObj->Serialize(serializeRoot);
        
        // read ::Json::value to std::string
        ::Json::StreamWriterBuilder builder;
        // builder["commentStyle"] = "None";
        // builder["indentation"] = "   ";  // or whatever you like
        output = ::Json::writeString(builder, serializeRoot);

        return true;
    }
    
    bool CJsonSerializer::Deserialize(IJsonSerializable* pObj, std::string& input )
    {
        if (pObj == NULL)
            return false;
        
        ::Json::Value deserializeRoot;
        std::string errs;
        
        ::Json::CharReaderBuilder builder;
        // builder["collectComments"] = false;

        std::istringstream sin(input);

        // read std::string to ::Json::value
        bool ok = ::Json::parseFromStream(builder, sin, &deserializeRoot, &errs);
        if(!ok) {
            std::cerr << "error : " << errs << std::endl;
        }
        
        // cast ::Json::value to object
        pObj->Deserialize(deserializeRoot);
        
        return true;
    }
}

#endif  // GRAPHQL_JSON_HPP__
