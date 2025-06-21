
import { useState } from "react";
import axios from "axios";
import CustomButton from "./CutomButton";

const InputBox = () => {
    const [inputText, setInputText] = useState("");
    const [responseMessage, setResponseMessage] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const validateLine = (line) => {
        const trimmed = line.trim();
        const amountPattern = /^(?!0+(?:[.,]0+)?$)(?=0*[1-9]|0*\d*[.,]\d*[1-9])\d+(?:[.,]\d+)?\s+.+$/;
        return amountPattern.test(trimmed);
    };

    const handleSubmit = async () => {
        const rawLines = inputText.split("\n");
        const trimmedLines = rawLines.map((line) => line.trim().toLowerCase());
        const errors = [];
        const validLines = [];
        const apiUrl = "/api/api/v1/expense/";

        trimmedLines.forEach((line, idx) => {
            if (line === "") return;
            if (validateLine(line)) {
            validLines.push(line);
            } else {
            errors.push(`Line ${idx + 1} is invalid: "${rawLines[idx]}"`);
            }
        });

        if (errors.length > 0) {
            setResponseMessage("Submission failed due to input errors:\n\n" + errors.join("\n"));
            return;
        }

        const payload = validLines.map((line) => ({ expense: line }));

        try {
            setIsLoading(true);
            await axios.post(apiUrl, payload);
            setResponseMessage("Expenses submitted successfully.");
            setInputText("");
        } catch (error) {
            setResponseMessage("Failed to submit expenses.");
            console.error(error);
        } finally {
            setIsLoading(false);
        }
    };

  return (
    <div className="w-full max-w-2xl mx-auto mt-10 p-6 bg-white shadow-lg rounded-xl border border-gray-200">
      <h2 className="text-2xl font-bold text-gray-800 mb-4">Enter Expenses</h2>
      <textarea
        className="w-full h-60 p-4 text-gray-700 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
        placeholder="Enter one expense per line..."
        value={inputText}
        onChange={(e) => setInputText(e.target.value)}
      ></textarea>
      <CustomButton 
        className="mt-4 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition duration-200 disabled:opacity-50"
        onSuccess={handleSubmit}
        label={isLoading ? "Submitting..." : "Submit Expenses"}
        disabled={isLoading || inputText.trim() === ""}
      />
      {responseMessage && (
        <div className="mt-4 text-sm whitespace-pre-wrap text-gray-700">{responseMessage}</div>
      )}
    </div>
  );
};

export default InputBox;
