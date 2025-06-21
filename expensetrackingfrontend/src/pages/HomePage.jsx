import { useState } from "react";
import CustomPieChart from '../components/CustomPieChart'
import InputBox from '../components/InputBox'
import Dropdown from '../components/Drowdown'

const HomePage = () => {
  const [selectedDate, setSelectedDate] = useState(null);
  const handleDateChange = (date) => {
    setSelectedDate(date);
  };
  return (
    <>
        <Dropdown onSelect={handleDateChange}/>
        <InputBox />
        <CustomPieChart timestamp={selectedDate}/>
    </>
  )
}

export default HomePage