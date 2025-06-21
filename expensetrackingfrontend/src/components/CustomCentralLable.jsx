const CustomCentralLabel = (props) => {
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

export default CustomCentralLabel;