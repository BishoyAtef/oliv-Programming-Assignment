import React, { useEffect, useRef, useState } from "react";
import { PieChart, Pie, Cell, LabelList, Label, ResponsiveContainer } from "recharts";
import axios from "axios";

const flattenTree = (node) => {
  if (!node.children || node.children.length === 0) {
    return [{ name: node.tag, value: node.sum, amount: node.amount || 0, children: [] }];
  }

  const childrenData = node.children.map((child) => ({
    name: child.tag,
    value: child.sum,
    children: child.children || [],
    amount: child.amount || 0
  }));

  if (node.amount && node.amount > 0) {
    childrenData.push({
      name: "#other",
      value: node.amount,
      amount: node.amount,
      children: []
    });
  }

  return childrenData;
};

const CenterLabel = ({ viewBox, total }) => {
  const { cx, cy } = viewBox;
  const totalText = `${total}K`;
  const textRef = useRef(null);
  const [textWidth, setTextWidth] = useState(0);

  useEffect(() => {
    if (textRef.current) {
      const bbox = textRef.current.getBBox();
      setTextWidth(bbox.width);
    }
  }, [total]);

  return (
    <g>
      <text  
        x={cx - (textWidth / 2)} 
        y={cy - 30} 
        textAnchor="start" 
        fill="gray"
        fontSize="18"
      >
        EGP
      </text>
      <text 
        ref={textRef}
        x={cx}
        y={cy + 25}
        textAnchor="middle"
        fill="black"
        fontSize="48"
        fontWeight="bold"
      >
        {totalText}
      </text>
    </g>
  );
};

const CenteredCustomLabel = (props) => {
  const { cx, cy, midAngle, innerRadius, outerRadius, name, color } = props;
  const RADIAN = Math.PI / 180;
  const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);

  const boxWidth = name.length * 8 + 16;
  const boxHeight = 24;
  const cornerRadius = 8;

  return (
    <g style={{ outline: 'none' }}>
      <rect
        x={x - boxWidth/2}
        y={y - boxHeight/2}
        rx={cornerRadius}
        ry={cornerRadius}
        width={boxWidth}
        height={boxHeight}
        fill={color}
        strokeWidth={1.5}
      />
      <text
        x={x}
        y={y}
        fill="gray"
        textAnchor="middle"
        dominantBaseline="middle"
        fontSize={14}
        fontWeight="bold"
      >
        {name}
      </text>
    </g>
  );
};

const ExpenseDonutChart = () => {
  const [navigationPath, setNavigationPath] = useState([]);
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/expenses/tree/latest-tree");
        const root = response.data;
        const flattened = flattenTree(root).map((item, index) => ({
          ...item,
          color: `hsl(${index * 37 % 360}, 60%, 60%)`
        }));
        const total = flattened.reduce((acc, item) => acc + item.value, 0);
        setData(flattened);
        setNavigationPath([{ name: root.tag, children: flattened, value: total }]);
      } catch (error) {
        console.error("Failed to fetch data:", error);
      }
    };
    fetchData();
  }, []);

  const currentNode = navigationPath[navigationPath.length - 1];

  const handleClick = (_, index) => {
    if (!currentNode || !currentNode.children || index == null || index < 0) return;
    const clicked = currentNode.children[index];

    if (!clicked.children || clicked.children.length === 0) return;

    const childrenData = flattenTree(clicked).map((item, i) => ({
      ...item,
      color: `hsl(${i * 37 % 360}, 60%, 60%)`
    }));

    const total = childrenData.reduce((acc, item) => acc + item.value, 0);
    setNavigationPath([...navigationPath, { ...clicked, children: childrenData, value: total }]);
  };

  const handleBack = () => {
    if (navigationPath.length > 1) {
      setNavigationPath(navigationPath.slice(0, -1));
    }
  };

  return (
    <div className="w-full flex flex-col items-center">
      <div style={{ width: '100%', height: '600px' }}>
        <ResponsiveContainer width="100%" height="100%">
          <PieChart>
            <Pie
              data={currentNode?.children || []}
              dataKey="value"
              cx="50%"
              cy="50%"
              innerRadius="94%"
              outerRadius="95%"
              paddingAngle={2}
              stroke="none"
              labelLine={false}
              label={<CenteredCustomLabel />} 
              onClick={handleClick}
              style={{ outline: 'none' }}
            >
              {(currentNode?.children || []).map((entry, index) => (
                <Cell key={`outer-${index}`} fill={entry.color} />
              ))}
            </Pie>
            <Pie
              data={currentNode?.children || []}
              dataKey="value"
              cx="50%"
              cy="50%"
              innerRadius="42%"
              outerRadius="75%"
              paddingAngle={2}
              labelLine={false}
              stroke="none"
              onClick={handleClick}
              style={{ outline: 'none' }}
            >
              {(currentNode?.children || []).map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry.color} />
              ))}
              <LabelList 
                dataKey="value" 
                position="inside" 
                formatter={(value) => `${value}K`}
                style={{ 
                  fill: "gray",
                  fontSize: 18, 
                  fontWeight: 'bold',
                  textShadow: 'none'
                }}
              />
              <Label content={<CenterLabel total={currentNode?.value || 0} />} position="center" />
            </Pie>
          </PieChart>
        </ResponsiveContainer>
      </div>

      {currentNode && currentNode.children && (
        <div className="mt-6 rounded-2xl p-6 w-[90%] bg-white shadow-xl border border-gray-200">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-gray-800">Breakdown of {currentNode.name}</h2>
            {navigationPath.length > 1 && (
              <button
                onClick={handleBack}
                className="bg-gray-200 hover:bg-gray-300 text-gray-800 px-3 py-1 rounded-md text-sm"
              >
                ← Back
              </button>
            )}
          </div>
          <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3">
            {currentNode.children.map((child, i) => (
              <li
                key={i}
                onClick={() => handleClick(null, i)}
                className="cursor-pointer bg-gray-100 hover:bg-gray-200 text-gray-700 p-3 rounded-xl transition-all duration-200"
              >
                <div className="font-semibold">{child.name}</div>
                <div>{child.value} EGP</div>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default ExpenseDonutChart;