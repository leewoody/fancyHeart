//XRole
#include "XRole.h"
static XRole* instance;

XRole* XRole::record(Value v){ 
	if(instance==NULL) instance=new XRole();
	if(instance->doc.IsNull()){
		std::string fullPath=FileUtils::getInstance()->fullPathForFilename("XRole.json");
		std::string str=FileUtils::getInstance()->getStringFromFile(fullPath);
		instance->doc.Parse<0>(str.c_str());
		if(instance->doc.HasParseError()){
			log("GetParseError %s",instance->doc.GetParseError());
		}
	}
	instance->v=v;
	return instance;
}
int XRole::getId(){
	return doc[v.asString().c_str()]["id"].GetInt();
}
int XRole::getNameID(){
	return doc[v.asString().c_str()]["nameID"].GetInt();
}
int XRole::getDes(){
	return doc[v.asString().c_str()]["des"].GetInt();
}
int XRole::getSlogan(){
	return doc[v.asString().c_str()]["slogan"].GetInt();
}
int XRole::getAttackType(){
	return doc[v.asString().c_str()]["attackType"].GetInt();
}
int XRole::getPos(){
	return doc[v.asString().c_str()]["pos"].GetInt();
}
std::string XRole::getAvatar(){
	return doc[v.asString().c_str()]["avatar"].GetString();
}
std::string XRole::getCard(){
	return doc[v.asString().c_str()]["card"].GetString();
}
std::string XRole::getIcon(){
	return doc[v.asString().c_str()]["icon"].GetString();
}
std::string XRole::getPotrait(){
	return doc[v.asString().c_str()]["potrait"].GetString();
}
int XRole::getCommonSkill(){
	return doc[v.asString().c_str()]["commonSkill"].GetInt();
}
int XRole::getSkill1(){
	return doc[v.asString().c_str()]["skill1"].GetInt();
}
int XRole::getSkill2(){
	return doc[v.asString().c_str()]["skill2"].GetInt();
}
int XRole::getSkill3(){
	return doc[v.asString().c_str()]["skill3"].GetInt();
}
int XRole::getSkill4(){
	return doc[v.asString().c_str()]["skill4"].GetInt();
}
int XRole::getSkill5(){
	return doc[v.asString().c_str()]["skill5"].GetInt();
}
int XRole::getSkill6(){
	return doc[v.asString().c_str()]["skill6"].GetInt();
}
int XRole::getSkill7(){
	return doc[v.asString().c_str()]["skill7"].GetInt();
}
int XRole::getLockGrid(){
	return doc[v.asString().c_str()]["lockGrid"].GetInt();
}
bool XRole::getIsRole(){
	return doc[v.asString().c_str()]["isRole"].GetInt();
}
