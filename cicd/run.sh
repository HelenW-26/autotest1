set -e

while getopts ":p:e:r:s:" flag
do
    case "${flag}" in
        p) PRODUCT=${OPTARG};;
        e) ENVIRONMENT=${OPTARG};;
        r) REGULATOR=${OPTARG};;
        s) SCRIPT=${OPTARG};;
#        all) all=${OPTARG};;
    esac
done

echo "Run test(s) against"
echo "Product $PRODUCT"
echo "Environment $ENVIRONMENT"
echo "Script $SCRIPT"

REGULATOR_LOWER= echo "${REGULATOR,,}"

PPATH="./src/main/resources/$PRODUCT/$REGULATOR/$ENVIRONMENT/*"

sed -i "s/ADMIN_URL/$(echo $ADMIN_URL | sed -e 's/\\/\\\\/g; s/\//\\\//g; s/&/\\\&/g')/g" $PPATH
sed -i "s/TRADE_URL/$(echo $TRADE_URL | sed -e 's/\\/\\\\/g; s/\//\\\//g; s/&/\\\&/g')/g" $PPATH
sed -i "s/CALLBACK_URL/$(echo $CALLBACK_URL | sed -e 's/\\/\\\\/g; s/\//\\\//g; s/&/\\\&/g')/g" $PPATH
sed -i "s/WEBSITE_URL/$(echo $WEBSITE_URL | sed -e 's/\\/\\\\/g; s/\//\\\//g; s/&/\\\&/g')/g" $PPATH
sed -i "s/REGISTER_URL/$(echo $REGISTER_URL | sed -e 's/\\/\\\\/g; s/\//\\\//g; s/&/\\\&/g')/g" $PPATH
sed -i "s/BRAND/$REGULATOR_LOWER/g" $PPATH
sed -i "s/ENV/$ENVIRONMENT/g" $PPATH

find $PPATH -type f -wholename "*$SCRIPT" -exec echo 'Testing {}' \;
scriptpath=$(find $PPATH -type f -wholename "*$SCRIPT" -exec readlink -f {} \;)

mvn clean test -DsuiteXmlFile=$scriptpath

