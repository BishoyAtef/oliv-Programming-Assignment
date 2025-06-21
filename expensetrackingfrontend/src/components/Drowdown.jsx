import { useState, useEffect } from "react";
import axios from "axios";

const Dropdown = ({ onSelect }) => {
  const [dates, setDates] = useState([]);
  const [selected, setSelected] = useState("");

  useEffect(() => {
    const apiUrl = "/api/api/v1/expense/msgs_timestamps"
    const fetchDates = async () => {
      try {
        const response = await axios.get(apiUrl);
        setDates(response.data);
      } catch (error) {
        console.error("Failed to fetch dates:", error);
      }
    };
    fetchDates();
  }, []);

  const handleChange = (e) => {
    const value = e.target.value;
    setSelected(value);
    if (onSelect) {
      onSelect(value);
    }
  };

  return (
    <div className="my-4">
      <label className="block text-gray-700 font-semibold mb-2">Select a Date:</label>
      <select
        value={selected}
        onChange={handleChange}
        className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        <option value="">-- Choose a date --</option>
        {dates.map((date, index) => (
          <option key={index} value={date}>{date}</option>
        ))}
      </select>
    </div>
  );
};

export default Dropdown;
