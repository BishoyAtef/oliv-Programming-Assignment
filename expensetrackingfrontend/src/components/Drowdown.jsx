import { useState } from "react";
import axios from "axios";

const Dropdown = ({ onSelect }) => {
  const [dates, setDates] = useState([]);
  const [selected, setSelected] = useState("");
  const [loading, setLoading] = useState(false);

  const fetchDates = async () => {
    setLoading(true);
    try {
      const response = await axios.get("/api/api/v1/expense/msgs_timestamps");
      const sortedDates = [...new Set(response.data)].sort((a, b) => b.localeCompare(a));
      setDates(sortedDates);
    } catch (error) {
      console.error("Failed to fetch dates:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleClick = () => {
    fetchDates();
  };

  const handleChange = (e) => {
    const value = e.target.value;
    setSelected(value);
    if (onSelect) {
      onSelect(value);
    }
  };

  return (
    <div className="my-4 w-full max-w-md mx-auto">
      <select
        value={selected}
        onFocus={handleClick}
        onChange={handleChange}
        className="w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
      >
        <option value="">
          {loading ? "Loading dates..." : "-- Choose a date --"}
        </option>
        {dates.map((date, index) => (
          <option key={index} value={date}>
            {date}
          </option>
        ))}
      </select>
    </div>
  );
};

export default Dropdown;

