import { useState, useEffect } from "react";
import axios from "axios";
import CustomPieChart from '../components/CustomPieChart'
import InputBox from '../components/InputBox'
import Dropdown from '../components/Drowdown'
import CustomButton from "../components/CutomButton";
import flattenTree from "../utils/FlattenTree";
import CustomTree from "../components/CustomTree"

const HomePage = () => {
  const [isFetching, setIsFetching] = useState(false);
  const [navigationPath, setNavigationPath] = useState([]);
  const [data, setData] = useState([]);

  const handleDateChange = async (timestamp) => {
    const url = (timestamp == null || timestamp == undefined || timestamp === "")? "/api/api/v1/expense-tree/latest" : `/api/api/v1/expense-tree/by-timestamp?value=${timestamp}`; 
    await fetchData(url);
  };

  const fetchLatestDate = async () => {
    await fetchData("/api/api/v1/expense-tree/latest");
  }

  const fetchData = async (url) => {
    try {
      setIsFetching(true);
      const response = await axios.get(url);
      const root = response.data;
      setData(root);
      const flattened = flattenTree(root).map((item, index) => ({
        ...item,
        color: `hsl(${index * 37 % 360}, 60%, 60%)`
      }));
      const total = flattened.reduce((acc, item) => acc + item.value, 0);
      setNavigationPath([{ name: root.tag, children: flattened, value: total }]);
    } catch (error) {
      console.error("Failed to fetch data:", error);
    } finally {
      setIsFetching(false);
    }
  };
  useEffect(() => {
    fetchData("/api/api/v1/expense-tree/latest");
  }, []);

  return (
    <>
      <Dropdown onSelect={handleDateChange}/>
      <div className="flex flex-col lg:flex-row gap-8 mt-10 px-6">
        <div className="w-full lg:w-1/4">
          <CustomTree data={data} />
        </div>
        <div className="w-full lg:w-1/2">
          <CustomButton 
            className="mt-6 text-center bg-gray-200 hover:bg-gray-300 font-bold text-gray-800 px-3 py-1 rounded-md text-sm"
            onSuccess={fetchLatestDate}
            label={isFetching? "Fetching..." : "Get Latest Expense Tree"}
            disabled={isFetching}
          />
          <CustomPieChart pieChartNavigationPath={navigationPath} />
        </div>
        <div className="w-full lg:w-1/4">
          <InputBox />
        </div>
      </div>
    </>
  )
}

export default HomePage